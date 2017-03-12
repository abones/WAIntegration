package com.whatsapp.integration.service;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.whatsapp.integration.model.MessageInfo;
import com.whatsapp.integration.model.QueuedMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Accessibility service that handles interactions with WhatsApp
 */
public class MyAccessibilityService extends AccessibilityService {
    // region Const

    private static final int MAX_SCROLLS = 30;

    private static final String WHATSAPP_ACTIVITY = "com.whatsapp.Conversation";
    private static final String WHATSAPP_INPUT_FIELD = "com.whatsapp:id/entry";
    private static final String WHATSAPP_SEND_BUTTON = "com.whatsapp:id/send";
    private static final String TAG = "WHATSAPP";
    public static final String ACTION_RECEIVE_MESSAGES = "com.whatsapp.integration.ACTION_RECEIVE_MESSAGES";
    public static final String ACTION_SEND_MESSAGE = "com.whatsapp.integration.ACTION_SEND_MESSAGE";
    public static final String EXTRA_MESSAGES = "com.whatsapp.integration.EXTRA_MESSAGES";
    public static final String EXTRA_MESSAGE = "com.whatsapp.integration.EXTRA_MESSAGE";
    private BroadcastReceiver sendMessageReceiver;

    // endregion Const

    // region Methods

    private void sendMessage(AccessibilityNodeInfo nodeInfo, String message) {
        List<AccessibilityNodeInfo> inputs = nodeInfo
            .findAccessibilityNodeInfosByViewId(WHATSAPP_INPUT_FIELD);

        if (inputs.isEmpty())
            return;

        Bundle arguments = new Bundle();
        arguments.putCharSequence(
            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
            message
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

    private synchronized void sendMessages(AccessibilityNodeInfo nodeInfo, String recipient) {
        if (!recipients.containsKey(recipient))
            return;

        Queue<QueuedMessage> queue = recipients.get(recipient);

        while (!queue.isEmpty())
            sendMessage(nodeInfo, queue.remove().getMessage());
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

    private int minValue(int a, int b) {
        return a < b ? a : b;
    }

    private void getMessages(AccessibilityNodeInfo nodeInfo, List<MessageInfo> target) {
        AccessibilityNodeInfo listNode = nodeInfo.findAccessibilityNodeInfosByViewId(
            "android:id/list").get(
            0);

        AccessibilityNodeInfo.CollectionInfo collectionInfo = listNode.getCollectionInfo();
        int scrollCount = minValue(MAX_SCROLLS, collectionInfo.getRowCount() / 10 + 1);

        for (int i = 0; i < scrollCount * 2; ++i) {
            listNode.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD.getId());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }

        for (int j = 0; j < listNode.getChildCount(); ++j) {
            AccessibilityNodeInfo messageGroup = listNode.getChild(j);

            if (messageGroup.getClass().isAssignableFrom(ViewGroup.class))
                continue;

            List<AccessibilityNodeInfo> messageTexts = messageGroup.findAccessibilityNodeInfosByViewId(
                "com.whatsapp:id/message_text");
            List<AccessibilityNodeInfo> messageDates = messageGroup.findAccessibilityNodeInfosByViewId(
                "com.whatsapp:id/date");

            if (messageTexts.isEmpty() || messageDates.isEmpty())
                continue;

            target.add(new MessageInfo(
                messageDates.get(0).getText().toString(),
                messageTexts.get(0).getText().toString()
            ));
        }
    }

    // endregion Methods

    // region Overrides of AccessibilityService

    private final Map<String , Queue<QueuedMessage>> recipients = new ConcurrentHashMap<>();

    private Queue<QueuedMessage> getRecipientQueue(String recipient) {
        Queue<QueuedMessage> result;
        if (recipients.containsKey(recipient))
            result = recipients.get(recipient);
        else {
            result = new ConcurrentLinkedQueue<>();
            recipients.put(recipient, result);
        }

        return result;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sendMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                QueuedMessage message = intent.getParcelableExtra(EXTRA_MESSAGE);

                Queue<QueuedMessage> queue = getRecipientQueue(message.getRecipient());

                queue.add(message);
                // TODO: start whatsapp app
            }
        };
        IntentFilter intentFilter = new IntentFilter(ACTION_SEND_MESSAGE);
        registerReceiver(sendMessageReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sendMessageReceiver);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!WHATSAPP_ACTIVITY.equals(event.getClassName())) {
            //event.recycle();
            return;
        }

        AccessibilityNodeInfo nodeInfo = event.getSource();
        //printNodeTree(nodeInfo, 0);
        ArrayList<MessageInfo> messages = new ArrayList<>();
        if (nodeInfo != null) {
            //sendMessage(nodeInfo);
            getMessages(nodeInfo, messages);
            Intent broadcastIntent = new Intent(ACTION_RECEIVE_MESSAGES);
            broadcastIntent.putParcelableArrayListExtra(EXTRA_MESSAGES, messages);
            sendBroadcast(broadcastIntent);

            sendMessages(nodeInfo, getRecipient(nodeInfo));
        }

        event.recycle();
    }

    private String getRecipient(AccessibilityNodeInfo nodeInfo) {
        return "+79138539660";
    }

    @Override
    public void onInterrupt() {
    }

    // endregion Overrides of AccessibilityService
}
