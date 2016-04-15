package com.marlon.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;

import com.marlon.android.widget.utils.FontCache;

public class EditTextPassword extends AppCompatEditText {

    private final int TEXT_PASSWORD = 129;
    private final int NUMBER_PASSWORD = 18;

    private boolean isPasswordVisible = true;
    private Drawable icon;
    private IconPosition iconPosition;
    private Typeface typeface;

    private int showPasswordIcon;
    private int hidePasswordIcon;
    private int inputType;

    public EditTextPassword(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(attrs);
        }
    }

    public EditTextPassword(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(attrs);
        }
    }

    private void init(AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.EditTextPassword);
            this.iconPosition = IconPosition.values()[typedArray.getInteger(R.styleable.EditTextPassword_iconPosition, IconPosition.RIGHT.ordinal())];
            this.showPasswordIcon = typedArray.getResourceId(R.styleable.EditTextPassword_showPasswordIcon, R.drawable.ic_show_password);
            this.hidePasswordIcon = typedArray.getResourceId(R.styleable.EditTextPassword_hidePasswordIcon, R.drawable.ic_hide_password);

            String path = typedArray.getString(R.styleable.EditTextPassword_fontPath);
            if (path != null) {
                this.typeface = FontCache.getInstance().put(path, getContext().getAssets());
                setFont(this.typeface);
            }

            typedArray.recycle();
        }

        this.inputType = getInputType();
        togglePassword();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void togglePassword() {
        if (this.isPasswordVisible) {
            setInputType(this.inputType);
        } else {
            if (this.inputType == TEXT_PASSWORD) {
                setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            }
        }

        this.isPasswordVisible = !this.isPasswordVisible;
        int textLength = getText().length();
        setSelection(textLength, textLength);
        setupIcon();
    }

    private void setupIcon() {
        this.icon = this.isPasswordVisible ? getDrawable(this.showPasswordIcon) : getDrawable(hidePasswordIcon);
        switch (this.iconPosition) {
            case LEFT: {
                setCompoundDrawablesWithIntrinsicBounds(this.icon, null, null, null);
                break;
            }
            case RIGHT: {
                setCompoundDrawablesWithIntrinsicBounds(null, null, this.icon, null);
                break;
            }
        }
        setCompoundDrawablePadding(10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            final int x = (int) event.getX();

            switch (this.iconPosition) {
                case LEFT: {
                    int iconWidth = this.icon.getBounds().width();
                    if (x >= getPaddingLeft() && x <= getPaddingLeft() + iconWidth) {
                        togglePassword();
                        event.setAction(MotionEvent.ACTION_CANCEL);
                    }
                    break;
                }
                case RIGHT: {
                    int iconWidth = this.icon.getBounds().width();
                    if (x >= (getWidth() - getPaddingLeft()) - iconWidth && x <= getWidth() + iconWidth - getPaddingRight()) {
                        togglePassword();
                        event.setAction(MotionEvent.ACTION_CANCEL);
                    }
                    break;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * @return show password icon
     */
    public
    @DrawableRes
    int getShowPasswordIcon() {
        return showPasswordIcon;
    }

    /**
     * Set show password icon
     *
     * @param showPasswordIcon
     */
    public void setShowPasswordIcon(@DrawableRes int showPasswordIcon) {
        this.showPasswordIcon = showPasswordIcon;
    }

    /**
     * @return hide password icon
     */
    public
    @DrawableRes
    int getHidePasswordIcon() {
        return hidePasswordIcon;
    }

    /**
     * Set hide password icon
     *
     * @param hidePasswordIcon
     */
    public void setHidePasswordIcon(@DrawableRes int hidePasswordIcon) {
        this.hidePasswordIcon = hidePasswordIcon;
    }

    /**
     * @return a boolean value indicating whether the password is being shown or not
     */
    public boolean isPasswordVisible() {
        return isPasswordVisible;
    }

    @Override
    public void setInputType(int inputType) {
        super.setInputType(inputType);
        setFont(this.typeface);
    }

    private void setFont(Typeface typeface) {
        if (typeface != null) {
            setTypeface(typeface);
        }
    }

    private Drawable getDrawable(int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }
}
