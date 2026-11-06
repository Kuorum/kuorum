'use strict';
// Phone validation of a REGULAR campaign (debate, survey, post...). Same modal as the contest vote, but without the kiosk mode.
const {describe, it, beforeEach} = require('node:test');
const assert = require('node:assert/strict');
const {createEnv, VALIDATED, pending} = require('../helpers/env');
const {COOKIE, pane} = require('../helpers/common');

describe('Other campaigns - phone validation', () => {
    let env;
    beforeEach(() => { env = createEnv(); });

    describe('predefined phone (taken from the user profile)', () => {
        it('shows the masked phone instead of asking for it', () => {
            env.primeValidation();
            env.u.nextValidationStep(pending({phone: true, phoneData: {predefinedPhone: true, phone: '6**'}}));
            assert.equal(env.displayed(pane('phone')), true);
            assert.equal(env.u.dataValidation.predefinedPhone, true);
            assert.equal(env.text('.modal-domain-validation-phone-step1-predefinedPhone-phone'), '6**');
            assert.equal(env.displayed('.modal-domain-validation-phone-step1-predefinedPhone'), true);
            assert.equal(env.displayed('.modal-domain-validation-phone-step1-inputPhone'), false);
        });
    });

    describe('anonymous action not allowed (the user must be registered)', () => {
        beforeEach(() => env.primeValidation({'data-allowAnonymousAction': null}));

        it('hides the second phone field, and repeating the phone is not needed to send', () => {
            env.u.openAndPrepareValidationModal();
            env.tick(300);
            assert.equal(env.u.are2ndPhoneFieldsHidden, true);
            assert.equal(env.displayed('.form-group.form-group-phone.second-phone'), false);
            env.u.showPhoneValidation();
            env.fill('#phoneNumber', '600');
            env.click('#validatePhoneDomain-modal-form-button-id');
            env.solveCaptcha();
            assert.equal(env.lastAjax().opts.data.phoneNumber, '600');
        });

        it('if the user closes the modal without validating it warns and reloads the page', () => {
            env.u.openAndPrepareValidationModal();
            env.tick(300);
            env.$('#domain-validation').trigger('hidden.bs.modal');
            assert.ok(env.has('display.error CLOSED-WITHOUT-VALIDATION'));
            assert.ok(env.events.some(e => e.startsWith('reloadPage') && e.endsWith('1000')));
        });
    });

    describe('once validated (no kiosk reset)', () => {
        it('closes the modal after 1 s and leaves the data as it is', () => {
            env.primeValidation();
            env.openModal();
            env.fill('#phoneNumber', '600');
            env.fill('#validationPhoneNumber', '600');
            env.u.nextValidationStep(VALIDATED);
            assert.ok(env.$('#validateDomain-modal-form-button-id .text-success').css('display') !== 'none');
            assert.equal(env.val('#validationPhoneNumber'), '', 'only the hidden sending fields are emptied');
            assert.equal(env.count('modal domain-validation hide'), 0);
            env.tick(1000);
            assert.equal(env.count('modal domain-validation hide'), 1);
            env.tick(300);
            assert.equal(env.val('#phoneNumber'), '600', 'the typed phone is not cleaned');
        });

        it('does not remove the anonymous user cookie nor forget the phone (contest-only behaviour)', () => {
            env.primeValidation();
            env.openModal();
            env.u.nextValidationStep(VALIDATED);
            env.tick(2000);
            assert.equal(env.count(COOKIE), 0);
        });
    });
});
