'use strict';
const {describe, it, beforeEach} = require('node:test');
const assert = require('node:assert/strict');
const {createEnv, SEND, VALIDATED} = require('./helpers/env');

const PHONE_STEP1 = '#domain-validation .modal-domain-validation-phone-step1';
const PHONE_STEP2 = '#domain-validation .modal-domain-validation-phone-step2';

/** A voter has used the modal: they sent their phone and are on the code step (code received by SMS). */
function voterUsedTheModal(env, response = SEND.smsFallback) {
    env.primeValidation();
    env.u.showPhoneValidation();
    env.openModal();
    env.sendPhone('600');
    env.respond(env.lastAjaxIndex(), response);
    env.tick(100);
}
const closeModal = (env) => env.$('#domain-validation').attr('data-shown', '0').data('isShown', false);

describe('Kiosk mode: the modal is left as new for the next voter', () => {
    let env;
    beforeEach(() => { env = createEnv(); });

    describe('prepareForNewVoter (when VOTE is clicked)', () => {
        beforeEach(() => { voterUsedTheModal(env); closeModal(env); });

        it('precondition: the previous voter left the modal dirty', () => {
            assert.equal(env.val('#phoneNumber'), '600');
            assert.equal(env.val('#phoneHash'), 'HASH-SMS');
            assert.equal(env.u.lastValidatedPhone, '600');
            assert.equal(env.u.currentChannel, 'SMS');
            assert.ok(env.u.resendTimers.SMS, 'resend countdown running');
            assert.equal(env.displayed(PHONE_STEP2), true);
        });

        it('empties phones, code and hidden fields', () => {
            env.u.prepareForNewVoter();
            for (const field of ['#phoneNumber', '#phoneNumber2', '#phoneCode', '#phoneHash', '#validationPhoneNumber', '#validationPhoneNumberPrefix']) {
                assert.equal(env.val(field), '', `${field} should be empty`);
            }
            assert.equal(env.val('#phoneNumberPrefix'), '0034');
            assert.equal(env.val('#phoneNumberPrefix2'), '0034');
        });

        it('forgets the last phone, the channel, the hash and the anti-spam countdown', () => {
            env.u.prepareForNewVoter();
            assert.equal(env.u.lastValidatedPhone, '');
            assert.equal(env.u.lastValidatedPrefix, '');
            assert.equal(env.u.lastValidatedHash, '');
            assert.equal(env.u.currentChannel, '');
            assert.equal(env.u.validated, false);
            assert.equal(env.u.resendTimers.SMS, undefined);
            assert.equal(env.u.successFunctionCallback, undefined);
            assert.equal(env.H.pendingTimers(), 0, 'no timers should be left alive');
        });

        it('goes back to step 1 (phone) and hides the code step', () => {
            env.u.prepareForNewVoter();
            assert.equal(env.displayed(PHONE_STEP1), true);
            assert.equal(env.displayed(PHONE_STEP2), false);
            assert.ok(env.$('#domain-validation .modal-domain-validation-step-tabs li.modal-domain-validation-step-tabs-phoneNumber').hasClass('active'));
            assert.ok(!env.$('#domain-validation .modal-domain-validation-step-tabs li.modal-domain-validation-step-tabs-phoneCode').hasClass('active'));
        });

        it('restores the WhatsApp texts and the resend buttons (nothing from the SMS channel of the previous voter)', () => {
            assert.equal(env.text('.modal-domain-validation-phone-step2 .help-block'), 'HELP-SMS');
            env.u.prepareForNewVoter();
            assert.equal(env.text('.modal-domain-validation-phone-step2 .help-block'), 'HELP-WA');
            assert.equal(env.text('#whatsAppNotReceived'), 'WA-NOT-RECEIVED');
            assert.equal(env.text('#btn-resend-whatsapp-action'), 'Resend WhatsApp');
            assert.equal(env.text('#btn-resend-sms-action'), 'Send SMS');
            assert.equal(env.text('#btn-resend-sms-action-fallback'), 'Resend SMS');
            assert.equal(env.attr('#btn-resend-sms-action', 'data-original-text'), 'Send SMS');
            assert.equal(env.attr('#validatePhoneDomain-modal-form-button-id', 'data-whatsapp-enabled'), 'true');
            for (const btn of ['#btn-resend-whatsapp-action', '#btn-resend-sms-action', '#btn-resend-sms-action-fallback']) {
                assert.equal(env.$(btn).prop('disabled'), false);
                assert.ok(!env.$(btn).hasClass('disabled'));
            }
        });

        it('removes the aria association between the code field and the help text of the previous voter', () => {
            assert.equal(env.attr('#phoneCode', 'aria-describedby'), 'help-text-phoneCode');
            env.u.prepareForNewVoter();
            assert.equal(env.attr('#phoneCode', 'aria-describedby'), undefined);
            assert.equal(env.attr('.modal-domain-validation-phone-step2 .help-block', 'id'), undefined);
        });

        it('removes the anonymous user cookie of the previous voter', () => {
            env.u.prepareForNewVoter();
            assert.equal(env.count('removeCookie KUORUM_USER_UUID'), 1);
        });

        it('resets the form validators and the marked errors', () => {
            env.$('#codeForm').data('validator', {resetForm: () => env.H.L('resetForm codeForm')});
            env.$('#phoneCode').addClass('error');
            env.u.prepareForNewVoter();
            assert.ok(env.has('resetForm codeForm'));
            assert.ok(!env.$('#phoneCode').hasClass('error'));
        });

        it('shows the second phone field again and hides the predefined phone block', () => {
            env.u.hide2ndPhoneFields();
            env.u.dataValidation.predefinedPhone = true;
            env.$('.modal-domain-validation-phone-step1-predefinedPhone').show();
            env.$('.modal-domain-validation-phone-step1-inputPhone').hide();
            env.$('.modal-domain-validation-phone-step1-predefinedPhone-phone').html('6**');
            env.u.prepareForNewVoter();
            assert.equal(env.u.are2ndPhoneFieldsHidden, false);
            assert.equal(env.displayed('.form-group.form-group-phone.second-phone'), true);
            assert.equal(env.displayed('.modal-domain-validation-phone-step1-predefinedPhone'), false);
            assert.equal(env.displayed('.modal-domain-validation-phone-step1-inputPhone'), true);
            assert.equal(env.text('.modal-domain-validation-phone-step1-predefinedPhone-phone'), '*****');
        });
    });

    describe('prepareForNewVoter with the modal open', () => {
        it('touches nothing: someone is using it', () => {
            voterUsedTheModal(env);
            env.u.prepareForNewVoter();
            assert.equal(env.val('#phoneNumber'), '600');
            assert.equal(env.val('#phoneHash'), 'HASH-SMS');
            assert.equal(env.u.lastValidatedPhone, '600');
            assert.equal(env.count('removeCookie KUORUM_USER_UUID'), 0);
        });
    });

    describe('silentLogout (when the vote finishes)', () => {
        beforeEach(() => { voterUsedTheModal(env); });

        it('with the modal visible: the JS state is cleaned right away and the DOM only when the fade-out ends', () => {
            env.u.silentLogout();
            // right away: memory clean, the modal is closing and the fields are not emptied yet (avoids flicker)
            assert.equal(env.u.lastValidatedPhone, '');
            assert.equal(env.u.resendTimers.SMS, undefined);
            assert.ok(env.has('modal domain-validation hide'));
            assert.equal(env.val('#phoneNumber'), '600');
            env.tick(299);
            assert.equal(env.val('#phoneNumber'), '600', 'the fade-out has not finished yet');
            env.tick(1);
            assert.equal(env.val('#phoneNumber'), '');
            assert.equal(env.val('#phoneHash'), '');
            assert.equal(env.displayed(PHONE_STEP1), true);
            assert.equal(env.step(), 'modal closed');
        });

        it('removes the anonymous user cookie', () => {
            env.u.silentLogout();
            assert.equal(env.count('removeCookie KUORUM_USER_UUID'), 1);
        });

        it('with the modal already closed: cleans the DOM immediately', () => {
            closeModal(env);
            env.u.silentLogout();
            assert.equal(env.val('#phoneNumber'), '');
            assert.equal(env.displayed(PHONE_STEP2), false);
            assert.ok(!env.has('modal domain-validation hide'));
        });

        it('is idempotent: calling it twice in a row breaks nothing', () => {
            env.u.silentLogout();
            env.u.silentLogout();
            env.tick(1000);
            assert.equal(env.val('#phoneNumber'), '');
            assert.equal(env.u.lastValidatedPhone, '');
        });
    });

    describe('pending automatic close after validating', () => {
        it('the reset cancels the 1 s close and does not close the modal of the next voter', () => {
            env.primeValidation();
            env.u.showPhoneValidation();
            env.openModal();
            env.u.nextValidationStep(VALIDATED);              // schedules the 1 s close
            assert.equal(env.H.pendingTimers(), 1);
            env.u.silentLogout();                             // the vote finished before that second
            env.tick(400);                                    // fade-out
            assert.equal(env.count('modal domain-validation hide'), 1);
            env.openModal();                                  // the next voter comes in
            env.tick(5000);
            assert.equal(env.count('modal domain-validation hide'), 1, 'no phantom close on the modal of the next voter');
            assert.equal(env.step(), 'step 1 (phone)');
        });
    });

    describe('captcha', () => {
        it('the reset neither fails nor calls grecaptcha.reset if the widget never ran', () => {
            env.primeValidation();
            assert.doesNotThrow(() => env.u.prepareForNewVoter());
            assert.doesNotThrow(() => env.u.silentLogout());
            assert.equal(env.events.filter(e => e.startsWith('grecaptcha.reset')).length, 0);
        });

        it('if the captcha ran and was never solved, the reset restarts it', () => {
            env.primeValidation();
            env.u.showPhoneValidation();
            env.$('#phoneNumber, #phoneNumber2').val('600');
            env.$('#validatePhoneDomain-modal-form-button-id').trigger('click'); // runs the captcha but it is not solved
            assert.ok(env.has('grecaptcha.execute 1'));
            closeModal(env);
            env.u.prepareForNewVoter();
            assert.equal(env.count('grecaptcha.reset 1'), 1);
            assert.equal(env.captcha.callback, undefined);
            assert.equal(env.captcha.grecaptchaResponse, '');
        });
    });
});
