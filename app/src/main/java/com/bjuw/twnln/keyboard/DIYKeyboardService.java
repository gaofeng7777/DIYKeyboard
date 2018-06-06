package com.bjuw.twnln.keyboard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.IBinder;
import android.text.InputType;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import com.bjuw.twnln.R;
import com.bjuw.twnln.pre.AppLicationPreferences;
import com.bjuw.twnln.view.LoadingView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class DIYKeyboardService extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {
    static final boolean DEBUG = false;

    static final boolean PROCESS_HARD_KEYS = true;

    private InputMethodManager mInputMethodManager;

    private DIYKeyBoardView mInputView;
    private LoadingView mLoadingView;
    private CompletionInfo[] mCompletions;

    private StringBuilder mComposing = new StringBuilder();
    private boolean mPredictionOn;
    private boolean mCompletionOn;
    private int mLastDisplayWidth;
    private boolean mCapsLock;
    private long mLastShiftTime;
    private long mMetaState;

    private DIYKeyboard mSymbolsKeyboard;
    private DIYKeyboard mSymbolsShiftedKeyboard;
    private DIYKeyboard mQwertyKeyboard;

    private DIYKeyboard mCurKeyboard;

    private String mWordSeparators;

    AppLicationPreferences appLicationPreferences;

    /**
     * Main initialization of the input method component.  Be sure to call
     * to super class.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mWordSeparators = getResources().getString(R.string.word_separators);
    }

    /**
     * This is the point where you can do all of your UI initialization.  It
     * is called after creation and any configuration change.
     */
    @Override
    public void onInitializeInterface() {
        if (mQwertyKeyboard != null) {
            // Configuration changes can happen after the keyboard gets recreated,
            // so we need to be able to re-build the keyboards if the available
            // space has changed.
            int displayWidth = getMaxWidth();
            if (displayWidth == mLastDisplayWidth) return;
            mLastDisplayWidth = displayWidth;
        }
        mQwertyKeyboard = new DIYKeyboard(this, R.xml.qwerty);
        mSymbolsKeyboard = new DIYKeyboard(this, R.xml.symbols);
        mSymbolsShiftedKeyboard = new DIYKeyboard(this, R.xml.symbols_shift);
    }

    /**
     * Called by the framework when your view for creating input needs to
     * be generated.  This will be called the first time your input method
     * is displayed, and every time it needs to be re-created such as due to
     * d configuration change.
     */
    @SuppressLint("NewApi")
    @Override
    public View onCreateInputView() {
        appLicationPreferences = AppLicationPreferences.getInstance(this);

        switch (appLicationPreferences.getKeyBoardThemesId()) {
            case 1:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes1, null);

                break;

            case 2:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes2, null);
                break;

            case 3:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes3, null);
                break;

            case 4:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes4, null);
                break;

            case 5:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes5, null);
                break;

            case 6:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes6, null);
                break;

            case 7:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes7, null);
                break;

            case 8:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes8, null);
                break;

            case 9:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes9, null);
                break;

            case 10:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes10, null);
                break;

            case 11:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes11, null);
                break;

            case 12:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes12, null);
                break;

            case 13:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes13, null);
                break;

            case 14:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes14, null);
                break;

            case 15:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes15, null);
                break;

            case 16:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes16, null);
                break;

            case 17:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes17, null);
                break;

            case 18:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes18, null);
                break;

            case 19:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes19, null);
                break;

            case 20:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes20, null);
                break;

            case 21:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes21, null);
                break;

            case 22:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes22, null);

                break;

            case 100:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.custom_keyboard, null);
                SharedPreferences mybg = getSharedPreferences("mybg", 0);
                String mybg_path = mybg.getString("mybg_path", "");
                Uri parse=null;
                parse = Uri.parse(mybg_path);
                if (parse!=null) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(parse));
                        Drawable drawable = new BitmapDrawable(bitmap);
                        mInputView.setBackground(drawable);
                    } catch (FileNotFoundException e) {
                    }
                }
                break;

            default:
                mInputView = (DIYKeyBoardView) getLayoutInflater().inflate(R.layout.keyboard_themes1, null);
                break;
        }

        mInputView.setOnKeyboardActionListener(this);
        setEnglishKeyboard(mQwertyKeyboard);
        return mInputView;
    }


    public Bitmap getPreferencesview(String fileName) {
        File image = new File(fileName);

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
            return null;
        }
        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                : bounds.outWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / 64;
        // opts.inSampleSize = originalSize;
        return BitmapFactory.decodeFile(image.getPath());
    }


    @SuppressLint("NewApi")
    private void setEnglishKeyboard(DIYKeyboard nextKeyboard) {
       /* final boolean shouldSupportLanguageSwitchKey = mInputMethodManager.shouldOfferSwitchingToNextInputMethod(getToken());*/
        /*use this method insted of above method because that not support bellow 19 api*/
        final boolean shouldSupportLanguageSwitchKey = mInputMethodManager.switchToNextInputMethod(getToken(), true);
        nextKeyboard.setChangelanguageKeyVisibility(shouldSupportLanguageSwitchKey);
        mInputView.setKeyboard(nextKeyboard);
    }

    /**
     * Called by the framework when your view for showing candidates needs to
     * be generated, like {@link #onCreateInputView}.
     */
    @Override
    public View onCreateCandidatesView() {
        mLoadingView = new LoadingView(this);
        mLoadingView.setKeyBoardService(this);
        return mLoadingView;
    }

    /**
     * This is the main point where we do our initialization of the input method
     * to begin operating on an application.  At this point we have been
     * bound to the client, and are now receiving all of the detailed information
     * about the target of our edits.
     */
    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        // Reset our state.  We want to do this even if restarting, because
        // the underlying state of the text editor could have changed in any way.
        mComposing.setLength(0);
        setCandidates();

        if (!restarting) {
            // Clear shift states.
            mMetaState = 0;
        }

        mPredictionOn = false;
        mCompletionOn = false;
        mCompletions = null;

        // We are now going to initialize our state based on the type of
        // text being edited.
        switch (attribute.inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
            case InputType.TYPE_CLASS_DATETIME:
                // Numbers and dates default to the symbols keyboard, with
                // no extra features.
                mCurKeyboard = mSymbolsKeyboard;
                break;

            case InputType.TYPE_CLASS_PHONE:
                // Phones will also default to the symbols keyboard, though
                // often you will want to have d dedicated phone keyboard.
                mCurKeyboard = mSymbolsKeyboard;
                break;

            case InputType.TYPE_CLASS_TEXT:
                // This is general text editing.  We will default to the
                // normal alphabetic keyboard, and assume that we should
                // be doing predictive text (showing candidates as the
                // user types).
                mCurKeyboard = mQwertyKeyboard;
                mPredictionOn = true;

                // We now look for d few special variations of text that will
                // modify our behavior.
                int variation = attribute.inputType & InputType.TYPE_MASK_VARIATION;
                if (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
                        variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    // Do not display predictions / what the user is typing
                    // when they are entering d password.
                    mPredictionOn = false;
                }

                if (variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        || variation == InputType.TYPE_TEXT_VARIATION_URI
                        || variation == InputType.TYPE_TEXT_VARIATION_FILTER) {
                    // Our predictions are not useful for e-mail addresses
                    // or URIs.
                    mPredictionOn = false;
                }

                if ((attribute.inputType & InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE) != 0) {
                    // If this is an auto-complete text view, then our predictions
                    // will not be shown and instead we will allow the editor
                    // to supply their own.  We only show the editor's
                    // candidates when in fullscreen mode, otherwise relying
                    // own it displaying its own UI.
                    mPredictionOn = false;
                    mCompletionOn = isFullscreenMode();
                }

                // We also want to look at the current state of the editor
                // to decide whether our alphabetic keyboard should start out
                // shifted.
                setShiftKeyState(attribute);
                break;

            default:
                // For all unknown input types, default to the alphabetic
                // keyboard with no special features.
                mCurKeyboard = mQwertyKeyboard;
                setShiftKeyState(attribute);
        }

        // Update the label on the enter key, depending on what the application
        // says it will do.
        mCurKeyboard.setKeyBoardConfiguration(getResources(), attribute.imeOptions);
    }

    /**
     * This is called when the user is done editing d field.  We can use
     * this to reset our state.
     */
    @Override
    public void onFinishInput() {
        super.onFinishInput();

        // Clear current composing text and candidates.
        mComposing.setLength(0);
        setCandidates();

        // We only hide the candidates window when finishing input on
        // d particular editor, to avoid popping the underlying application
        // up and down if the user is entering text into the bottom of
        // its window.
        setCandidatesViewShown(false);

        mCurKeyboard = mQwertyKeyboard;
        if (mInputView != null) {
            mInputView.closing();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        // Apply the selected keyboard to the input view.
        setInputView(onCreateInputView());
        setEnglishKeyboard(mCurKeyboard);
        mInputView.closing();

        final InputMethodSubtype subtype = mInputMethodManager.getCurrentInputMethodSubtype();
        mInputView.setKeyBoardSpaceKey(subtype);
    }

    @Override
    public void onCurrentInputMethodSubtypeChanged(InputMethodSubtype subtype) {
        if (mInputView != null) {
            mInputView.setKeyBoardSpaceKey(subtype);
        }
    }

    /**
     * Deal with the editor reporting movement of its cursor.
     */
    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                candidatesStart, candidatesEnd);

        if (mComposing.length() > 0 && (newSelStart != candidatesEnd
                || newSelEnd != candidatesEnd)) {
            mComposing.setLength(0);
            setCandidates();
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    @Override
    public void onDisplayCompletions(CompletionInfo[] completions) {
        if (mCompletionOn) {
            mCompletions = completions;
            if (completions == null) {
                setKeyBoardSuggestions(null, false, false);
                return;
            }

            List<String> stringList = new ArrayList<String>();
            for (int i = 0; i < completions.length; i++) {
                CompletionInfo ci = completions[i];
                if (ci != null) stringList.add(ci.getText().toString());
            }
            setKeyBoardSuggestions(stringList, true, true);
        }
    }

    /**
     * This translates incoming hard key events in to edit operations on an
     * InputConnection.  It is only needed when using the
     * PROCESS_HARD_KEYS option.
     */
    private boolean translateKeysetDown(int keyCode, KeyEvent event) {
        mMetaState = MetaKeyKeyListener.handleKeyDown(mMetaState,
                keyCode, event);
        int c = event.getUnicodeChar(MetaKeyKeyListener.getMetaState(mMetaState));
        mMetaState = MetaKeyKeyListener.adjustMetaAfterKeypress(mMetaState);
        InputConnection ic = getCurrentInputConnection();
        if (c == 0 || ic == null) {
            return false;
        }

        boolean dead = false;

        if ((c & KeyCharacterMap.COMBINING_ACCENT) != 0) {
            dead = true;
            c = c & KeyCharacterMap.COMBINING_ACCENT_MASK;
        }

        if (mComposing.length() > 0) {
            char accent = mComposing.charAt(mComposing.length() - 1);
            int composed = KeyEvent.getDeadChar(accent, c);

            if (composed != 0) {
                c = composed;
                mComposing.setLength(mComposing.length() - 1);
            }
        }

        onKey(c, null);

        return true;
    }

    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // The InputMethodService already takes care of the back
                // key for us, to dismiss the input method if it is shown.
                // However, our keyboard could be showing d pop-up window
                // that back should dismiss, so we first allow it to do that.
                if (event.getRepeatCount() == 0 && mInputView != null) {
                    if (mInputView.handleBack()) {
                        return true;
                    }
                }
                break;

            case KeyEvent.KEYCODE_DEL:
                // Special handling of the delete key: if we currently are
                // composing text for the user, we want to modify that instead
                // of let the application to the delete itself.
                if (mComposing.length() > 0) {
                    onKey(Keyboard.KEYCODE_DELETE, null);
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_ENTER:
                // Let the underlying text editor always handle these.
                return false;

            default:
                // For all other keys, if we want to do transformations on
                // text being entered with d hard keyboard, we need to process
                // it and do the appropriate action.
                if (PROCESS_HARD_KEYS) {
                    if (keyCode == KeyEvent.KEYCODE_SPACE
                            && (event.getMetaState() & KeyEvent.META_ALT_ON) != 0) {
                        // A silly example: in our input method, Alt+Space
                        // is d shortcut for 'android' in lower case.
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            // First, tell the editor that it is no longer in the
                            // shift state, since we are consuming this.
                            ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
                            keyDownUp(KeyEvent.KEYCODE_A);
                            keyDownUp(KeyEvent.KEYCODE_N);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            keyDownUp(KeyEvent.KEYCODE_R);
                            keyDownUp(KeyEvent.KEYCODE_O);
                            keyDownUp(KeyEvent.KEYCODE_I);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            // And we consume this event.
                            return true;
                        }
                    }
                    if (mPredictionOn && translateKeysetDown(keyCode, event)) {
                        return true;
                    }
                }
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * Use this to monitor key events being delivered to the application.
     * We get first crack at them, and can either resume them or let them
     * continue to the app.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // If we want to do transformations on text being entered with d hard
        // keyboard, we need to process the up events to update the meta key
        // state we are tracking.
        if (PROCESS_HARD_KEYS) {
            if (mPredictionOn) {
                mMetaState = MetaKeyKeyListener.handleKeyUp(mMetaState,
                        keyCode, event);
            }
        }

        return super.onKeyUp(keyCode, event);
    }

    /**
     * Helper function to commit any text being composed in to the editor.
     */
    private void commitKeyBoardTyped(InputConnection inputConnection) {
        if (mComposing.length() > 0) {
            inputConnection.commitText(mComposing, mComposing.length());
            mComposing.setLength(0);
            setCandidates();
        }
    }

    /**
     * Helper to update the shift state of our keyboard based on the initial
     * editor state.
     */
    private void setShiftKeyState(EditorInfo attr) {
        if (attr != null && mInputView != null && mQwertyKeyboard == mInputView.getKeyboard()) {
            int caps = 0;
            EditorInfo ei = getCurrentInputEditorInfo();
            if (ei != null && ei.inputType != InputType.TYPE_NULL) {
                caps = getCurrentInputConnection().getCursorCapsMode(attr.inputType);
            }
            mInputView.setShifted(mCapsLock || caps != 0);
        }
    }

    /**
     * Helper to determine if d given character code is alphabetic.
     */
    private boolean isASDFbet(int code) {
        if (Character.isLetter(code)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper to send d key down / key up pair to the current editor.
     */
    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    /**
     * Helper to send d character to the editor as raw key events.
     */
    private void upKey(int keyCode) {
        switch (keyCode) {
            case '\n':
                keyDownUp(KeyEvent.KEYCODE_ENTER);
                break;
            default:
                if (keyCode >= '0' && keyCode <= '9') {
                    keyDownUp(keyCode - '0' + KeyEvent.KEYCODE_0);
                } else {
                    getCurrentInputConnection().commitText(String.valueOf((char) keyCode), 1);
                }
                break;
        }
    }

    // Implementation of KeyboardViewListener

    public void onKey(int primaryCode, int[] keyCodes) {
        if (isletterSeparator(primaryCode)) {
            // Handle separator
            if (mComposing.length() > 0) {
                commitKeyBoardTyped(getCurrentInputConnection());
            }
            upKey(primaryCode);
            setShiftKeyState(getCurrentInputEditorInfo());
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            dealwithBackspace();
        } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            dealwithShift();
        } else if (primaryCode == Keyboard.KEYCODE_CANCEL) {
            dealwithClose();
            return;
        } else if (primaryCode == DIYKeyBoardView.KEYCODE_LANGUAGE_SWITCH) {
            dealwithLanguageSwitch();
            return;
        } else if (primaryCode == DIYKeyBoardView.KEYCODE_OPTIONS) {
            // Show d menu or somethin'
        } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE
                && mInputView != null) {
            Keyboard current = mInputView.getKeyboard();
            if (current == mSymbolsKeyboard || current == mSymbolsShiftedKeyboard) {
                setEnglishKeyboard(mQwertyKeyboard);
            } else {
                setEnglishKeyboard(mSymbolsKeyboard);
                mSymbolsKeyboard.setShifted(false);
            }
        } else {
            dealwithCharacter(primaryCode, keyCodes);
        }
    }

    public void onText(CharSequence text) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        ic.beginBatchEdit();
        if (mComposing.length() > 0) {
            commitKeyBoardTyped(ic);
        }
        ic.commitText(text, 0);
        ic.endBatchEdit();
        setShiftKeyState(getCurrentInputEditorInfo());
    }

    /**
     * Update the list of available candidates from the current composing
     * text.  This will need to be filled in by however you are determining
     * candidates.
     */
    private void setCandidates() {
        if (!mCompletionOn) {
            if (mComposing.length() > 0) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(mComposing.toString());
                setKeyBoardSuggestions(list, true, true);
            } else {
                setKeyBoardSuggestions(null, false, false);
            }
        }
    }

    public void setKeyBoardSuggestions(List<String> suggestions, boolean completions,
                                       boolean typedWordValid) {
        if (suggestions != null && suggestions.size() > 0) {
            setCandidatesViewShown(true);
        } else if (isExtractViewShown()) {
            setCandidatesViewShown(true);
        }
        if (mLoadingView != null) {
            mLoadingView.setKeyBoardSuggestions(suggestions, completions, typedWordValid);
        }
    }

    private void dealwithBackspace() {
        final int length = mComposing.length();
        if (length > 1) {
            mComposing.delete(length - 1, length);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            setCandidates();
        } else if (length > 0) {
            mComposing.setLength(0);
            getCurrentInputConnection().commitText("", 0);
            setCandidates();
        } else {
            keyDownUp(KeyEvent.KEYCODE_DEL);
        }
        setShiftKeyState(getCurrentInputEditorInfo());
    }

    private void dealwithShift() {
        if (mInputView == null) {
            return;
        }

        Keyboard currentKeyboard = mInputView.getKeyboard();
        if (mQwertyKeyboard == currentKeyboard) {
            // Alphabet keyboard
            checkSwitchCapsLock();
            mInputView.setShifted(mCapsLock || !mInputView.isShifted());
        } else if (currentKeyboard == mSymbolsKeyboard) {
            mSymbolsKeyboard.setShifted(true);
            setEnglishKeyboard(mSymbolsShiftedKeyboard);
            mSymbolsShiftedKeyboard.setShifted(true);
        } else if (currentKeyboard == mSymbolsShiftedKeyboard) {
            mSymbolsShiftedKeyboard.setShifted(false);
            setEnglishKeyboard(mSymbolsKeyboard);
            mSymbolsKeyboard.setShifted(false);
        }
    }

    private void dealwithCharacter(int primaryCode, int[] keyCodes) {

        if (isInputViewShown()) {
            if (mInputView.isShifted()) {
                primaryCode = Character.toUpperCase(primaryCode);
            }
        }
        if (mPredictionOn) {
            mComposing.append((char) primaryCode);
            getCurrentInputConnection().setComposingText(mComposing, 1);
            setShiftKeyState(getCurrentInputEditorInfo());
            setCandidates();
        } else {
            getCurrentInputConnection().commitText(Character.toString((char) primaryCode), 1);
        }
    }

    private void dealwithClose() {
        commitKeyBoardTyped(getCurrentInputConnection());
        requestHideSelf(0);
        mInputView.closing();
    }

    private IBinder getToken() {
        final Dialog dialog = getWindow();
        if (dialog == null) {
            return null;
        }
        final Window window = dialog.getWindow();
        if (window == null) {
            return null;
        }
        return window.getAttributes().token;
    }

    @SuppressLint("NewApi")
    private void dealwithLanguageSwitch() {
        mInputMethodManager.switchToNextInputMethod(getToken(), false /* onlyCurrentIme */);
    }

    private void checkSwitchCapsLock() {
        long now = System.currentTimeMillis();
        if (mLastShiftTime + 800 > now) {
            mCapsLock = !mCapsLock;
            mLastShiftTime = 0;
        } else {
            mLastShiftTime = now;
        }
    }

    private String getletterSeparators() {
        return mWordSeparators;
    }

    public boolean isletterSeparator(int code) {
        String separators = getletterSeparators();
        return separators.contains(String.valueOf((char) code));
    }

    public void selectDefaultCandidate() {
        selectSuggestionManually(0);
    }

    public void selectSuggestionManually(int index) {
        if (mCompletionOn && mCompletions != null && index >= 0
                && index < mCompletions.length) {
            CompletionInfo ci = mCompletions[index];
            getCurrentInputConnection().commitCompletion(ci);
            if (mLoadingView != null) {
                mLoadingView.clearView();
            }
            setShiftKeyState(getCurrentInputEditorInfo());
        } else if (mComposing.length() > 0) {
            // If we were generating candidate suggestions for the current
            // text, we would commit one of them here.  But for this sample,
            // we will just commit the current text.
            commitKeyBoardTyped(getCurrentInputConnection());
        }
    }

    public void swipeRight() {
        if (mCompletionOn) {
            selectDefaultCandidate();
        }
    }

    public void swipeLeft() {
        dealwithBackspace();
    }

    public void swipeDown() {
        dealwithClose();
    }

    public void swipeUp() {
    }

    public void onPress(int primaryCode) {
    }

    public void onRelease(int primaryCode) {
    }
}
