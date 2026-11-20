'use strict';
const fs = require('fs');
const path = require('path');
const vm = require('vm');
const {JSDOM} = require('jsdom');

const JS_DIR = path.resolve(__dirname, '../../../web-app/js');
const read = (...parts) => fs.readFileSync(path.join(...parts), 'utf8');

/**
 * Creates a jsdom page with the front's validation modal and loads the REAL files from web-app/js:
 * jquery.min.js, modal-validation.js and, optionally, campaigns/contestApplication.js.
 * Everything that depends on the server or the browser is replaced in helpers/stubs.js.
 */
function createEnv(options = {}) {
    const dom = new JSDOM(read(__dirname, 'fixture.html'), {runScripts: 'outside-only', url: 'https://kuorum.test/'});
    const ctx = dom.getInternalVMContext();
    const run = (code, filename) => new vm.Script(code, {filename}).runInContext(ctx);
    run(read(JS_DIR, 'jquery.min.js'), 'jquery.min.js');
    run(read(__dirname, 'stubs.js'), 'stubs.js');
    // MODAL_VALIDATION_FILE allows testing another version of the file (e.g. the one in HEAD) to check that the tests detect regressions
    const modalValidation = process.env.MODAL_VALIDATION_FILE ? fs.readFileSync(path.resolve(process.env.MODAL_VALIDATION_FILE), 'utf8') : read(JS_DIR, 'modal-validation.js');
    run(modalValidation, 'modal-validation.js');
    // A top-level `const` is visible to other scripts by name but is not a window property: expose it so the tests can reach it.
    // No-op for files that declare these with `var`.
    run("['userValidatedByDomain', 'captcha'].forEach(function (name) {"
        + " if (!(name in window) && typeof eval(name) !== 'undefined') { window[name] = eval(name); } });", 'expose-globals.js');
    if (options.contestApplication) {
        run(read(JS_DIR, 'campaigns', 'contestApplication.js'), 'contestApplication.js');
    }
    const w = dom.window;
    const $ = w.$;
    const H = w.H;
    if (options.contestApplication) {
        H.tick(0); // jQuery runs $(function(){...}) asynchronously: this is where the VOTE button listeners get registered
    }

    const env = {
        window: w, $, H,
        get u() { return w.userValidatedByDomain; },
        get captcha() { return w.captcha; },
        get events() { return H.log; },
        get ajaxCalls() { return H.ajaxCalls; },
        count: (event) => H.log.filter(e => e === event).length,
        has: (event) => H.log.includes(event),
        tick: (ms) => H.tick(ms),
        respond: (i, payload) => H.respond(i, payload),
        fail: (i) => H.fail(i),
        lastAjax: () => H.ajaxCalls[H.ajaxCalls.length - 1],
        lastAjaxIndex: () => H.ajaxCalls.length - 1,
        // "Would be seen once the modal is open": ignores whether the modal itself is closed (useful to check the DOM state after a reset)
        displayed: (selector) => {
            const els = $(selector).toArray();
            return els.length > 0 && els.every(el => {
                for (let e = el; e && e.id !== 'domain-validation'; e = e.parentNode) {
                    if (e.style && e.style.display === 'none') { return false; }
                }
                return true;
            });
        },
        visible: (selector) => $(selector).toArray().every(el => H.visible(el)) && $(selector).length > 0,
        val: (selector) => $(selector).val(),
        text: (selector) => $(selector).first().text().trim(),
        attr: (selector, name) => $(selector).attr(name),
        fill: (selector, value) => $(selector).val(value),
        click: (selector) => $(selector).trigger('click'),
        openModal: () => $('#domain-validation').attr('data-shown', '1').data('isShown', true),
        solveCaptcha: (token = 'TOKEN') => w.captchaSolvedCallback(token),
        setValidForms: (valid) => { w.__valid = valid; },
        // "VOTE" button with the attributes rendered by the GSP; `attrs` overrides any of them
        voteButton: (attrs = {}) => {
            const $b = $('#vote-main');
            Object.entries(attrs).forEach(([k, v]) => v === null ? $b.removeAttr(k) : $b.attr(k, v));
            return $b;
        },
        /** Campaign button with the attributes rendered by the GSP; `attrs` overrides (or removes with null) any of them */
        button: (attrs = {}) => {
            const $b = $('<a>').attr({
                'data-loggedUser': '', 'data-allowAnonymousAction': 'true', 'data-campaignId': '3517',
                'data-campaignValidationActive': 'true', 'data-campaignGroupValidationActive': '',
                'data-ajaxAnonymousValidationChecker': '/anon-check', 'data-useralias': ''
            });
            Object.entries(attrs).forEach(([k, v]) => v === null ? $b.removeAttr(k) : $b.attr(k, v));
            return $b;
        },
        /** The action the campaign wants to run once the user is validated (leaves the 'action executed' event) */
        action: () => new w.userValidatedByDomain.ExcutableFunctionCallback((p) => H.L('action executed', p), 'PARAMS'),
        /** Prepares the state the way a validation check call leaves it (initDataValidation + initVariables). */
        primeValidation: (attrs = {}) => {
            const $b = env.button(attrs);
            const callback = new w.userValidatedByDomain.ExcutableFunctionCallback(() => H.L('callback executed'), 'P');
            w.userValidatedByDomain.initDataValidation($b, callback);
            w.userValidatedByDomain.initVariables();
            return $b;
        },
        /** Types the phone (in both fields) and clicks "Confirm phone number", solving the captcha. */
        sendPhone: (phone = '600') => {
            $('#phoneNumber, #phoneNumber2').val(phone);
            $('#validatePhoneDomain-modal-form-button-id').trigger('click');
            w.captchaSolvedCallback('TOKEN');
        },
        /** Visible state of the modal, for high-level assertions */
        step: () => {
            if (!H.visible($('#domain-validation')[0])) { return 'modal closed'; }
            if ($('.modal-domain-validation-phone-step2').toArray().filter(e => e.id === '').some(e => H.visible(e))) { return 'step 2 (code)'; }
            if ($('.modal-domain-validation-phone-step1').toArray().filter(e => e.id === '').some(e => H.visible(e))) { return 'step 1 (phone)'; }
            return 'otro';
        }
    };
    return env;
}

// Server responses to the code sending (POST /sendSms)
const phone = (extra) => Object.assign({success: true, validated: false, validationPhoneNumber: '600', validationPhoneNumberPrefix: '0034'}, extra);
const SEND = {
    whatsapp: phone({hash: 'HASH-WA', actualChannel: 'WHATSAPP', isWhatsAppEnabled: true}),
    smsFallback: phone({hash: 'HASH-SMS', actualChannel: 'SMS', isWhatsAppEnabled: true}),        // WhatsApp enabled in the domain but the sending falls back to SMS
    smsOnly: phone({hash: 'HASH-ONLY', actualChannel: 'SMS', isWhatsAppEnabled: false}),
    alreadyValidated: phone({validated: true, hash: null, actualChannel: 'null', isWhatsAppEnabled: true,
        pendingValidations: {tokenMailValidation: {success: true}, censusValidation: {success: true}, codeValidation: {success: true}, phoneValidation: {success: true, data: {}}}}),
    rejected: {success: false, validated: false, msg: 'INVALID PHONE'}
};
// Responses of the checks / validations
const pending = (steps) => ({
    success: true, validated: false,
    pendingValidations: {
        tokenMailValidation: {success: !steps.token}, censusValidation: {success: !steps.census},
        codeValidation: {success: !steps.code}, phoneValidation: {success: !steps.phone, data: steps.phoneData || {}}
    }
});
const VALIDATED = {success: true, validated: true};

module.exports = {createEnv, SEND, pending, VALIDATED};
