'use strict';
const {describe, it, beforeEach} = require('node:test');
const assert = require('node:assert/strict');
const {createEnv, SEND, VALIDATED} = require('./helpers/env');

const STEP1 = '#domain-validation .modal-domain-validation-phone-step1';
const STEP2 = '#domain-validation .modal-domain-validation-phone-step2';
const HELP = '.modal-domain-validation-phone-step2 .help-block';
const BACK = '#validatePhoneCodeDomain-modal-form-button-back';
const CONFIRM = '#validatePhoneDomain-modal-form-button-id';
const VALIDATE_CODE = '#validatePhoneCodeDomain-modal-form-button-id';

function openPhoneStep(env, attrs) {
    env.primeValidation(attrs);
    env.u.showPhoneValidation();
    env.openModal();
}
/** Clicks "Confirm phone number", solves the captcha and answers as the server. */
function sendAndRespond(env, response, phone = '600') {
    env.sendPhone(phone);
    env.respond(env.lastAjaxIndex(), response);
    env.tick(100);
}
// Objects created inside jsdom belong to another "realm": normalize them before comparing with deepEqual
const {plain} = require('./helpers/common');
const isDisplayed = (env, selector) => env.$(selector).first().css('display') !== 'none';

describe('Phone flow (code sent by WhatsApp or SMS)', () => {
    let env;
    beforeEach(() => { env = createEnv(); openPhoneStep(env); });

    describe('sending the code', () => {
        it('asks for the code on the AUTO channel with phone, prefix, captcha token and campaign', () => {
            env.sendPhone('600');
            assert.equal(env.ajaxCalls.length, 1);
            const {opts} = env.lastAjax();
            assert.equal(opts.type, 'POST');
            assert.equal(opts.url, '/sendSms');
            assert.deepEqual(plain(opts.data), {
                campaignId: '3517', phoneNumberPrefix: '0034', phoneNumber: '600',
                'g-recaptcha-response': 'TOKEN', allowAnonymousAction: 'true', channel: 'AUTO'
            });
        });

        it('if the two phones do not match it shows the error and sends nothing', () => {
            env.fill('#phoneNumber', '600');
            env.fill('#phoneNumber2', '601');
            env.click(CONFIRM);
            env.solveCaptcha();
            assert.equal(env.ajaxCalls.length, 0);
            assert.equal(env.text('.text-error-data'), 'NONMATCH');
            assert.equal(env.step(), 'step 1 (phone)');
        });

        it('if the form is not valid it sends nothing', () => {
            env.setValidForms(false);
            env.sendPhone('600');
            assert.equal(env.ajaxCalls.length, 0);
        });

        it('if the server rejects the phone it shows its message and stays on step 1 without countdown', () => {
            sendAndRespond(env, SEND.rejected);
            assert.equal(env.text('.text-error-data'), 'INVALID PHONE');
            assert.equal(env.displayed('#domain-validation .text-danger'), true);
            assert.equal(env.step(), 'step 1 (phone)');
            assert.equal(env.val('#phoneHash'), '');
            assert.equal(env.u.resendTimers.WHATSAPP, undefined);
            assert.equal(env.u.resendTimers.SMS, undefined);
        });

        it('the channel is requested with the right strategy and SMS is the default channel', () => {
            for (const [asked, expected] of [['SMS', 'SMS'], ['WHATSAPP', 'WHATSAPP'], ['AUTO', 'AUTO'], ['OTHER', 'SMS']]) {
                env.u.sendValidationCode(env.$(CONFIRM), asked);
                env.solveCaptcha();
                assert.equal(env.lastAjax().opts.data.channel, expected, `requested channel ${asked}`);
            }
        });
    });

    describe('response: WhatsApp', () => {
        beforeEach(() => sendAndRespond(env, SEND.whatsapp));

        it('goes to the code step with the WhatsApp texts', () => {
            assert.equal(env.step(), 'step 2 (code)');
            assert.equal(env.u.currentChannel, 'WHATSAPP');
            assert.equal(env.text(HELP), 'HELP-WA');
            assert.equal(env.text('#whatsAppNotReceived'), 'LINK-WA');
            assert.ok(env.events.some(e => e.startsWith('rules phoneCode') && e.includes('ERR-WA')), 'code error message for WhatsApp');
            assert.equal(env.attr('#phoneCode', 'aria-describedby'), 'help-text-phoneCode');
        });

        it('stores the hash and phone of the sending and shows the WhatsApp-only instructions', () => {
            assert.equal(env.val('#phoneHash'), 'HASH-WA');
            assert.equal(env.val('#validationPhoneNumber'), '600');
            assert.equal(env.val('#validationPhoneNumberPrefix'), '0034');
            assert.equal(env.displayed('#ui-block-whatsapp'), true);
            assert.equal(env.displayed('#ui-block-sms-only'), false);
            assert.notEqual(env.$('.whatsapp-only-help').first().css('display'), 'none');
        });

        it('blocks the WhatsApp resend for 60 s with a countdown and enables it when it ends', () => {
            assert.equal(env.text('#btn-resend-whatsapp-action'), 'RESEND-WA (60s)');
            assert.equal(env.$('#btn-resend-whatsapp-action').prop('disabled'), true);
            assert.equal(env.text('#btn-resend-sms-action'), 'SEND-SMS');
            assert.equal(env.$('#btn-resend-sms-action').prop('disabled'), false);
            env.tick(59000);
            assert.equal(env.text('#btn-resend-whatsapp-action'), 'RESEND-WA (1s)');
            env.tick(1000);
            assert.equal(env.text('#btn-resend-whatsapp-action'), 'RESEND-WA');
            assert.equal(env.$('#btn-resend-whatsapp-action').prop('disabled'), false);
            assert.equal(env.u.resendTimers.WHATSAPP, undefined);
        });

        it('a click on the blocked button resends nothing', () => {
            const before = env.ajaxCalls.length;
            env.click('#btn-resend-whatsapp-action');
            assert.equal(env.ajaxCalls.length, before);
        });
    });

    describe('response: WhatsApp enabled in the domain but the sending falls back to SMS', () => {
        beforeEach(() => sendAndRespond(env, SEND.smsFallback));

        it('shows the SMS texts, because the code was sent by SMS', () => {
            assert.equal(env.u.currentChannel, 'SMS');
            assert.equal(env.text(HELP), 'HELP-SMS');
            assert.equal(env.text('#whatsAppNotReceived'), 'LINK-SMS');
            assert.ok(env.events.some(e => e.startsWith('rules phoneCode') && e.includes('ERR-SMS')));
        });

        it('keeps WhatsApp available: the code can be requested by WhatsApp and the SMS button counts down', () => {
            assert.equal(env.attr(CONFIRM, 'data-whatsapp-enabled'), 'true');
            assert.equal(env.displayed('#ui-block-whatsapp'), true);
            assert.equal(env.$('.whatsapp-only-help').first().css('display'), 'none');
            assert.equal(env.text('#btn-resend-whatsapp-action'), 'SEND-WA');
            assert.equal(env.$('#btn-resend-whatsapp-action').prop('disabled'), false);
            assert.equal(env.text('#btn-resend-sms-action'), 'RESEND-SMS (60s)');
            assert.equal(env.$('#btn-resend-sms-action').prop('disabled'), true);
        });
    });

    describe('response: domain without WhatsApp (SMS only)', () => {
        beforeEach(() => sendAndRespond(env, SEND.smsOnly));

        it('hides everything about WhatsApp and shows only the SMS resend with its countdown', () => {
            assert.equal(env.attr(CONFIRM, 'data-whatsapp-enabled'), 'false');
            assert.equal(env.displayed('#ui-block-whatsapp'), false);
            assert.equal(env.displayed('#ui-block-sms-only'), true);
            assert.equal(env.text('#btn-resend-sms-action-fallback'), 'Resend SMS (60s)');
            assert.ok(env.$('#btn-resend-sms-action-fallback').hasClass('disabled'));
            assert.equal(env.$('#btn-resend-sms-action-fallback').css('pointer-events'), 'none');
            env.tick(60000);
            assert.equal(env.text('#btn-resend-sms-action-fallback'), 'Resend SMS');
            assert.ok(!env.$('#btn-resend-sms-action-fallback').hasClass('disabled'));
            assert.equal(env.$('#btn-resend-sms-action-fallback').css('pointer-events'), 'auto');
        });
    });

    describe('response: the server already knew the phone (no code is sent)', () => {
        beforeEach(() => sendAndRespond(env, SEND.alreadyValidated));

        it('does not show the code screen', () => {
            assert.equal(env.displayed(STEP2), false);
        });

        it('continues with the pending action and closes the modal after 1 s', () => {
            assert.ok(env.has('callback executed'));
            assert.equal(env.count('modal domain-validation hide'), 0);
            env.tick(1000);
            assert.equal(env.count('modal domain-validation hide'), 1);
        });
    });

    describe('resending the code', () => {
        beforeEach(() => sendAndRespond(env, SEND.whatsapp));

        it('asking for SMS after WhatsApp empties the code and the help text without hiding it, and fills it with the SMS one', () => {
            env.fill('#phoneCode', '9');
            env.click('#btn-resend-sms-action');
            env.solveCaptcha();
            assert.equal(env.lastAjax().opts.data.channel, 'SMS');
            assert.equal(env.val('#phoneCode'), '');
            assert.equal(env.text(HELP), '');
            assert.equal(isDisplayed(env, HELP), true, 'the help text must not be left hidden');
            env.respond(env.lastAjaxIndex(), SEND.smsFallback);
            env.tick(100);
            assert.equal(env.text(HELP), 'HELP-SMS');
            assert.equal(isDisplayed(env, HELP), true);
            assert.equal(env.u.currentChannel, 'SMS');
        });

        it('after a fallback to SMS, when the countdown ends WhatsApp can be requested again with its texts', () => {
            env.click('#btn-resend-sms-action');
            env.solveCaptcha();
            env.respond(env.lastAjaxIndex(), SEND.smsFallback);
            env.tick(100);
            assert.equal(env.$('#btn-resend-whatsapp-action').prop('disabled'), true, 'the earlier WhatsApp sending is still in its countdown');
            env.tick(60000);
            env.click('#btn-resend-whatsapp-action');
            env.solveCaptcha();
            assert.equal(env.lastAjax().opts.data.channel, 'WHATSAPP');
            env.respond(env.lastAjaxIndex(), SEND.whatsapp);
            env.tick(100);
            assert.equal(env.text(HELP), 'HELP-WA');
            assert.equal(env.u.currentChannel, 'WHATSAPP');
        });
    });

    describe('validating the code', () => {
        beforeEach(() => sendAndRespond(env, SEND.smsFallback));

        it('sends phone, prefix, hash, code, channel and campaign', () => {
            env.fill('#phoneCode', '1234');
            env.click(VALIDATE_CODE);
            const {opts} = env.lastAjax();
            assert.equal(opts.url, '/validatePhone');
            assert.deepEqual(plain(opts.data), {
                campaignId: '3517', validationPhoneNumber: '600', validationPhoneNumberPrefix: '0034',
                phoneHash: 'HASH-SMS', phoneCode: '1234', channel: 'SMS'
            });
        });

        it('if the form is not valid it sends nothing', () => {
            env.setValidForms(false);
            env.click(VALIDATE_CODE);
            assert.equal(env.ajaxCalls.length, 1);
        });

        it('a wrong code shows the error, stays on the code step and can be retried with the same data', () => {
            env.fill('#phoneCode', '1111');
            env.click(VALIDATE_CODE);
            env.respond(env.lastAjaxIndex(), {success: false, validated: false, msg: 'WRONG CODE'});
            assert.equal(env.text('.text-error-data'), 'WRONG CODE');
            assert.equal(env.displayed('#domain-validation .text-danger'), true);
            assert.equal(env.step(), 'step 2 (code)');
            env.fill('#phoneCode', '2222');
            env.click(VALIDATE_CODE);
            const {data} = env.lastAjax().opts;
            assert.equal(data.phoneCode, '2222');
            assert.equal(data.validationPhoneNumber, '600');
            assert.equal(data.phoneHash, 'HASH-SMS');
        });

        it('a network error shows the warning and the modal stays usable', () => {
            env.fill('#phoneCode', '1234');
            env.click(VALIDATE_CODE);
            env.fail(env.lastAjaxIndex());
            assert.ok(env.has('display.error Error validating the sms'));
            assert.equal(env.displayed('#domain-validation .loading'), false);
            assert.equal(env.step(), 'step 2 (code)');
        });

        it('a correct code continues with the pending action', () => {
            env.fill('#phoneCode', '0000');
            env.click(VALIDATE_CODE);
            env.respond(env.lastAjaxIndex(), VALIDATED);
            assert.ok(env.has('callback executed'));
        });
    });

    describe('anti-spam shortcut: Back and Confirm with the same phone', () => {
        beforeEach(() => sendAndRespond(env, SEND.smsFallback));

        it('Back goes to step 1 and empties the hash and the phone of the sending', () => {
            env.click(BACK);
            assert.equal(env.step(), 'step 1 (phone)');
            assert.equal(env.val('#phoneHash'), '');
            assert.equal(env.val('#validationPhoneNumber'), '');
        });

        it('within 60 s it does not resend and goes back to the code step restoring the data so it can be validated', () => {
            env.click(BACK);
            const sends = env.ajaxCalls.length;
            env.click(CONFIRM);
            assert.equal(env.ajaxCalls.length, sends, 'no new code is requested');
            assert.equal(env.count('grecaptcha.execute 1'), 1, 'the captcha is not even launched again');
            assert.equal(env.step(), 'step 2 (code)');
            assert.equal(env.val('#validationPhoneNumber'), '600');
            assert.equal(env.val('#validationPhoneNumberPrefix'), '0034');
            assert.equal(env.val('#phoneHash'), 'HASH-SMS');
            env.fill('#phoneCode', '1234');
            env.click(VALIDATE_CODE);
            assert.deepEqual(plain(env.lastAjax().opts.data), {
                campaignId: '3517', validationPhoneNumber: '600', validationPhoneNumberPrefix: '0034',
                phoneHash: 'HASH-SMS', phoneCode: '1234', channel: 'SMS'
            });
        });

        it('with another phone it does send a new code', () => {
            env.click(BACK);
            const sends = env.ajaxCalls.length;
            env.sendPhone('700');
            assert.equal(env.ajaxCalls.length, sends + 1);
            assert.equal(env.lastAjax().opts.data.phoneNumber, '700');
        });

        it('after 60 s it also sends a new code even with the same phone', () => {
            env.click(BACK);
            env.tick(60000);
            const sends = env.ajaxCalls.length;
            env.sendPhone('600');
            assert.equal(env.ajaxCalls.length, sends + 1);
        });
    });
});
