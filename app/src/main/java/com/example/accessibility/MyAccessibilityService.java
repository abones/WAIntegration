package com.example.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Accessibility service that handles interactions with WhatsApp
 */
public class MyAccessibilityService extends AccessibilityService {
    // region Const

    private static final String WHATSAPP_ACTIVITY = "com.whatsapp.Conversation";
    private static final String WHATSAPP_INPUT_FIELD = "com.whatsapp:id/entry";
    private static final String WHATSAPP_SEND_BUTTON = "com.whatsapp:id/send";

    // endregion Const

    // region Methods

    private void sendMessage(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> inputs = nodeInfo
            .findAccessibilityNodeInfosByViewId(WHATSAPP_INPUT_FIELD);

        if (inputs.isEmpty())
            return;

        Bundle arguments = new Bundle();
        arguments.putCharSequence(
            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
            "Hello, this is a message"
        );
        for (AccessibilityNodeInfo node : inputs) {
            node.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }

        List<AccessibilityNodeInfo> buttons = nodeInfo
            .findAccessibilityNodeInfosByViewId(WHATSAPP_SEND_BUTTON);
        for (AccessibilityNodeInfo node : buttons) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    // endregion Methods

    // region Overrides of AccessibilityService

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!WHATSAPP_ACTIVITY.equals(event.getClassName())) {
            event.recycle();
            return;
        }

        AccessibilityNodeInfo nodeInfo = event.getSource();
        //if (nodeInfo != null)
        //    sendMessage(nodeInfo);

        event.recycle();
    }

    @Override
    public void onInterrupt() {
    }

    // endregion Overrides of AccessibilityService
}
