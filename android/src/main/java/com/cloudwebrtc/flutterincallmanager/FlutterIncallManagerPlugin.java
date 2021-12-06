package com.cloudwebrtc.flutterincallmanager;

import android.app.Activity;
import android.content.Context;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterIncallManagerPlugin */
public class FlutterIncallManagerPlugin implements FlutterPlugin, ActivityAware {
    private static final String CHANNEL_ID = "FlutterInCallManager.Method";

    private MethodChannel channel;
    private MethodCallHandlerImpl handler;

    /**
     * Plugin registration.
     *
     * <p>Must be called when the application is created.
     */
    public static void registerWith(Registrar registrar) {
        final FlutterIncallManagerPlugin plugin = new FlutterIncallManagerPlugin();
        plugin.setupChannel(registrar.messenger(), registrar.context(), registrar.activity());
    }

    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        setupChannel(binding.getBinaryMessenger(), binding.getApplicationContext(), null);
    }

    @Override
    public void onDetachedFromEngine(FlutterPluginBinding binding) {
        teardownChannel();
    }

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        handler.setActivity(binding.getActivity());
    }

    @Override
    public void onDetachedFromActivity() {
        handler.setActivity(null);
    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    private void setupChannel(BinaryMessenger messenger, Context context, Activity activity) {
        channel = new MethodChannel(messenger, CHANNEL_ID);
        handler = new MethodCallHandlerImpl(context, activity, messenger);
        channel.setMethodCallHandler(handler);
    }

    private void teardownChannel() {
        channel.setMethodCallHandler(null);
        channel = null;
        handler = null;
    }
}