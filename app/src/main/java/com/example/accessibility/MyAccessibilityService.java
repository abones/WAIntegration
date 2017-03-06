package com.example.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
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
    private static final String TAG = "WHATSAPP";

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

    private String explode(CharSequence string, int times) {
        if (times == 0)
            return "";

        return new String(new char[times]).replace("\0", string);
    }

    // requires flagReportViewIds|flagIncludeNotImportantViews
    private void printNodeTree(AccessibilityNodeInfo nodeInfo, int level) {
        Log.d(
            TAG,
            String.format(
                "%s %s (t:\"%s\" c:\"%s\")",
                explode("-", level),
                nodeInfo.getViewIdResourceName(),
                nodeInfo.getText(),
                nodeInfo.getClassName()
            )
        );

        int newLevel = level + 1;
        for (int i = 0; i < nodeInfo.getChildCount(); ++i)
            printNodeTree(nodeInfo.getChild(i), newLevel);
    }

    private void printMessages(AccessibilityNodeInfo nodeInfo) {
        AccessibilityNodeInfo list = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/list").get(
            0);

        for (int i = 0; i < list.getChildCount(); ++i) {
            AccessibilityNodeInfo messageGroup = list.getChild(i);

            if (messageGroup.getClass().isAssignableFrom(ViewGroup.class))
                continue;

            List<AccessibilityNodeInfo> messageTexts = messageGroup.findAccessibilityNodeInfosByViewId(
                "com.whatsapp:id/message_text");
            List<AccessibilityNodeInfo> messageDates = messageGroup.findAccessibilityNodeInfosByViewId(
                "com.whatsapp:id/date");

            if (messageTexts.isEmpty() || messageDates.isEmpty())
                continue;

            Log.d(
                TAG,
                String.format(
                    "%s %s",
                    messageDates.get(0).getText(),
                    messageTexts.get(0).getText()
                )
            );
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
        //printNodeTree(nodeInfo, 0);
        if (nodeInfo != null)
            //sendMessage(nodeInfo);
            printMessages(nodeInfo);

        event.recycle();
    }

    @Override
    public void onInterrupt() {
    }

    // endregion Overrides of AccessibilityService
}
