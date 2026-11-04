'use strict';
// Census validation: pending step and form submission.
const {describe, it, beforeEach} = require('node:test');
const assert = require('node:assert/strict');
const {createEnv, VALIDATED, pending} = require('../helpers/env');
const {pane, tab} = require('../helpers/common');

const SUBMIT = '#validateDomain-modal-form-button-id';

describe('Other campaigns - census validation', () => {
    let env;
    beforeEach(() => { env = createEnv(); env.primeValidation(); });

    describe('pending step', () => {
        it('shows the census pane with its tab active', () => {
            env.u.nextValidationStep(pending({census: true}));
            assert.equal(env.displayed(pane('census')), true);
            assert.equal(env.displayed(pane('phone')), false);
            assert.ok(env.$(tab('census')).hasClass('active'));
        });
    });

    describe('form', () => {
        it('sends the serialized form plus the campaign', () => {
            env.fill('#ndi', 'N1');
            env.fill('#postalCode', '28001');
            env.click(SUBMIT);
            const {opts} = env.lastAjax();
            assert.equal(opts.url, '/census');
            assert.equal(opts.data, 'ndi=N1&postalCode=28001&campaignId=3517');
        });

        it('sends nothing if the form is not valid', () => {
            env.setValidForms(false);
            env.click(SUBMIT);
            assert.equal(env.ajaxCalls.length, 0);
        });

        it('a network error warns with its usual text and removes the spinner', () => {
            env.click(SUBMIT);
            env.fail(env.lastAjaxIndex());
            assert.ok(env.has('display.error Error validating user'));
            assert.equal(env.displayed('#domain-validation .loading'), false);
        });

        it('once validated it runs the pending action', () => {
            env.click(SUBMIT);
            env.respond(env.lastAjaxIndex(), VALIDATED);
            assert.ok(env.has('callback executed'), 'the action left ready by primeValidation()');
        });
    });
});
