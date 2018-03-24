package com.example.michael.latinhelper;

import com.honu.aloha.BaseWelcomeActivity;
import com.honu.aloha.PageDescriptor;

/**
 * Created by michael on 2/27/16.
 */
public class WelcomeActivity extends BaseWelcomeActivity {
    @Override
    public void createPages() {
        addPage(new PageDescriptor(R.string.wish, R.string.madeBy, R.drawable.school, R.color.colorWelcome));
    }
}
