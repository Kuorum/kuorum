'use strict';
// Email token validation step.
const {describe, it, beforeEach} = require('node:test');
const assert = require('node:assert/strict');
const {createEnv, pending} = require('../helpers/env');
const {pane} = require('../helpers/common');

describe('Other campaigns - email token validation', () => {
    let env;
    beforeEach(() => { env = createEnv(); env.primeValidation(); });

    it('shows only the email token pane when it is the pending validation', () => {
        env.u.nextValidationStep(pending({token: true}));
        assert.equal(env.displayed(pane('tokenMail')), true);
        for (const other of ['census', 'phone', 'customCode', 'groupCampaign']) {
            assert.equal(env.displayed(pane(other)), false, other);
        }
    });
});
