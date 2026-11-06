'use strict';
// Which pending validation step the modal shows first, and how an unsuccessful response is reported.
const {describe, it, beforeEach} = require('node:test');
const assert = require('node:assert/strict');
const {createEnv, pending} = require('../helpers/env');
const {pane} = require('../helpers/common');

describe('Other campaigns - validation step priority', () => {
    let env;
    beforeEach(() => { env = createEnv(); env.primeValidation(); });

    it('the order is email token, census, custom code, phone', () => {
        env.u.nextValidationStep(pending({token: true, census: true, code: true, phone: true}));
        assert.equal(env.displayed(pane('tokenMail')), true);
        env.u.nextValidationStep(pending({census: true, code: true, phone: true}));
        assert.equal(env.displayed(pane('census')), true);
        env.u.nextValidationStep(pending({code: true, phone: true}));
        assert.equal(env.displayed(pane('customCode')), true);
    });

    it('an unsuccessful response shows its error message', () => {
        env.openModal();
        env.u.nextValidationStep({success: false, validated: false, msg: 'NOT VALID'});
        assert.equal(env.text('.text-error-data'), 'NOT VALID');
    });
});
