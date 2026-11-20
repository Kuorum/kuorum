# Regression tests: validation flows of the other campaigns

`modal-validation.js` is shared by debates, surveys, posts, events, petitions, district proposals and contest applications.
The kiosk mode (full reset of the modal and silent logout after each vote) is **only for contest applications**.
These tests pin the ORIGINAL behaviour of every other flow so the kiosk mode never leaks into them.

One file per validation flow:

| File | Flow |
|---|---|
| `entryPoint.test.js` | `executeClickButtonHandlingValidations` and `ExcutableFunctionCallback`: the entry point every campaign uses |
| `loginEntryPoint.test.js` | `handleLoginAndValidationUser`: registered user / registration required |
| `emailToken.test.js` | email token validation |
| `census.test.js` | census validation |
| `customCode.test.js` | custom code validation |
| `groupValidation.test.js` | campaign group validation |
| `phoneValidation.test.js` | phone validation of a regular campaign (no kiosk reset) |
| `stepPriority.test.js` | which pending step is shown first |
