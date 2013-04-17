package com.twinsant.android2trello;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class AndrelloWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		
		Log.d(AndrelloWidgetProvider.class.getName(), "onUpdate: " + N);
		
		for (int i=0; i<N; i++) {
			int appWidgetId = appWidgetIds[i];
			Intent intent = new Intent(context, StartActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.andrello_appwidget);
			views.setOnClickPendingIntent(R.id.card, pendingIntent);
			AndrelloApplication app = (AndrelloApplication)context.getApplicationContext();
			views.setTextViewText(R.id.card, app.getWidgetCard());
			
			appWidgetManager.updateAppWidget(appWidgetId, views);
			//super.onUpdate(context, appWidgetManager, appWidgetIds);
		}
	}

}
