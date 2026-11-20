'use strict';
// Custom code validation: pending step and form submission.
const {describe, it, beforeEach} = require('node:test');
const assert = require('node:assert/strict');
const {createEnv, pending} = require('../helpers/env');
const {pane, tab, plain} = require('../helpers/common');

const SUBMIT = '#validateCustomCodeDomain-modal-form-button-id';

describe('Other campaigns - custom code validation', () => {
    let env;
    beforeEach(() => { env = createEnv(); env.primeValidation(); });

    describe('pending step', () => {
        it('shows the custom code pane with its tab active', () => {
            env.u.nextValidationStep(pending({code: true}));
            assert.equal(env.displayed(pane('customCode')), true);
            assert.ok(env.$(tab('customCode')).hasClass('active'));
        });
    });

    describe('form', () => {
        it('sends the code and the campaign', () => {
            env.fill('#customCode', 'ABC');
            env.click(SUBMIT);
            const {opts} = env.lastAjax();
            assert.equal(opts.url, '/customCode');
            assert.deepEqual(plain(opts.data), {campaignId: '3517', customCode: 'ABC'});
        });

        it('a network error warns with its usual text', () => {
            env.click(SUBMIT);
            env.fail(env.lastAjaxIndex());
            assert.ok(env.has('display.error Error validating the sms'));
        });
    });
});
