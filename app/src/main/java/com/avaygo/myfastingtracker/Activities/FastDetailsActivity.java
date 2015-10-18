package com.avaygo.myfastingtracker.activities;import android.app.Activity;import android.app.AlertDialog;import android.content.DialogInterface;import android.content.Intent;import android.os.Bundle;import android.support.v4.app.NavUtils;import android.view.LayoutInflater;import android.view.Menu;import android.view.MenuItem;import android.view.View;import android.widget.EditText;import android.widget.TextView;import com.avaygo.myfastingtracker.R;import com.avaygo.myfastingtracker.databases.LogDataSource;import java.text.SimpleDateFormat;import java.util.Calendar;import java.util.Locale;public class FastDetailsActivity extends Activity {    private LogDataSource logDataSource;    private TextView textNote;    private String userNote;    private long rowId;    private int day, month, year;    private boolean noteChanged = false;    private boolean setResult = false;    private boolean deleteRecord = false;    SimpleDateFormat titleFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault());    SimpleDateFormat dayDateMonthFormat = new SimpleDateFormat("EEEE d MMMM", Locale.getDefault());    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_fast_details);        logDataSource = new LogDataSource(this);        final Intent intent = getIntent();        Calendar recordDate = Calendar.getInstance(); // The date that the fast was logged in to the database        Calendar startDate = Calendar.getInstance(); // The date that the fast started on        Calendar endDate = Calendar.getInstance(); // The date that the fast was intended to end on        StringBuilder stringBuilder = new StringBuilder();        //Get the intent extras that are to be used later on in the body text        int fastDuration = intent.getIntExtra("FAST_DURATION", 0);        int percentComplete = intent.getIntExtra("PERCENT_COMPLETE", 0);        recordDate.setTimeInMillis(intent.getLongExtra("LOG_TIMESTAMP", 0));        startDate.setTimeInMillis(intent.getLongExtra("START_TIMESTAMP", 0));        endDate.setTimeInMillis(intent.getLongExtra("END_TIMESTAMP", 0));        rowId = intent.getLongExtra("ROW_ID", 0);        day = intent.getIntExtra("DAY", 0);        month = intent.getIntExtra("MONTH", 0);        year = intent.getIntExtra("YEAR", 0);        TextView textEndTitle = (TextView) findViewById(R.id.text_end_title);        textEndTitle.setText(titleFormat.format(recordDate.getTime()));        TextView textblock1 = (TextView) findViewById(R.id.text_block_1);        stringBuilder.append("This fast started at " + timeFormat.format(startDate.getTime()) + " on " +                dayDateMonthFormat.format(startDate.getTime()) + " and ended at " + timeFormat.format(recordDate.getTime())                + " on " + dayDateMonthFormat.format(recordDate.getTime()) + ". ");        //If the user has achieved 100% on the fast then it is unnecessary to show the intend finishing time as well.        if (percentComplete == 100) {            stringBuilder.append(System.getProperty("line.separator"));            stringBuilder.append(System.getProperty("line.separator"));            stringBuilder.append("It was supposed to last for " + fastDuration + " hours. ");        }        else {            stringBuilder.append(System.getProperty("line.separator"));            stringBuilder.append(System.getProperty("line.separator"));            if(fastDuration > 1) {                stringBuilder.append("It was supposed to last for " + fastDuration + " hours, ending at " + timeFormat.format(endDate.getTime())                        + " on " + dayDateMonthFormat.format(endDate.getTime()) + ". ");            }            else{                stringBuilder.append("It was supposed to last for " + fastDuration + " hour, ending at " + timeFormat.format(endDate.getTime())                        + " on " + dayDateMonthFormat.format(endDate.getTime()) + ". ");            }        }        if (percentComplete == 100) {            if (fastDuration > 1) {                stringBuilder.append("You achieved " + fastDuration + " hours. Well done! ");            } else {                stringBuilder.append("You achieved " + fastDuration + " hour. Well done! ");            }        } else if (percentComplete < 50) {            //How long they had left            stringBuilder.append(System.getProperty("line.separator"));            stringBuilder.append(System.getProperty("line.separator"));            long timeLeft = endDate.getTimeInMillis() - recordDate.getTimeInMillis();            stringBuilder.append("You completed " + percentComplete + "% and had " + longToHours(timeLeft) + " and " + longToMins(timeLeft) +                    " remaining. ");        }        else if (percentComplete < 100 && percentComplete >= 50) {            //How long they achieved            stringBuilder.append(System.getProperty("line.separator"));            stringBuilder.append(System.getProperty("line.separator"));            long timeCompleted = recordDate.getTimeInMillis() - startDate.getTimeInMillis();            stringBuilder.append("You achieved " + longToHours(timeCompleted) + " and " + longToMins(timeCompleted) +                    " for " + percentComplete + "% completion. ");        }        //Finally set the textview        textblock1.setText(stringBuilder.toString());        logDataSource.open();        textNote = (TextView) findViewById(R.id.text_note);        userNote = logDataSource.getNote(rowId);        textNote.setText(userNote);        logDataSource.close();    }    public String longToHours(long millisToCalculate) {        int iHours = (int) ((millisToCalculate / (1000 * 60 * 60)) % 24);        int totalMinutes = (int) (millisToCalculate / 1000);        String hours;        String length;        hours = Integer.toString(iHours);        if (iHours == 1) {            length = hours + " hour";        } else {            length = hours + " hours";        }        //Fasts less than an hour don't get recorded        return length;    }    public String longToMins(long millisToCalculate) {        int iMinutes = (int) ((millisToCalculate / (1000 * 60)) % 60);        int totalMinutes = (int) (millisToCalculate / 1000);        String minutes;        String length;        //Formatting code, add leading zero if less than 10 minutes        if (iMinutes < 10) {            minutes = ("0" + Integer.toString(iMinutes));        } else {            minutes = Integer.toString(iMinutes);        }        if (iMinutes == 1) {            length = minutes + " minute";        } else {            length = minutes + " minutes";        }        return length;    }    @Override    public boolean onCreateOptionsMenu(Menu menu) {        // Inflate the menu; this adds items to the action bar if it is present.        getMenuInflater().inflate(R.menu.menu_fast_details, menu);        return true;    }    @Override    public boolean onOptionsItemSelected(MenuItem item) {        // Handle action bar item clicks here. The action bar will        // automatically handle clicks on the Home/Up button, so long        // as you specify a parent activity in AndroidManifest.xml.        int id = item.getItemId();        //noinspection SimplifiableIfStatement        switch (id) {            case android.R.id.home:                Intent parentIntent = NavUtils.getParentActivityIntent(this);                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP                        | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);                startActivity(parentIntent);                finish();                return true;            case (R.id.action_edit):                displayAlert();                return true;            case (R.id.action_delete):                deleteRecord();                return true;        }        return super.onOptionsItemSelected(item);    }    @Override    public void finish() {        if(setResult) {            Intent intent = new Intent();            intent.putExtra("DAY", day);            intent.putExtra("MONTH", month);            intent.putExtra("YEAR", year);            intent.putExtra("NOTE_CHANGED", noteChanged);            intent.putExtra("DELETE_RECORD", deleteRecord);            setResult(Activity.RESULT_OK, intent);        }        super.finish();    }    private void displayAlert() {        LayoutInflater layoutInflater = LayoutInflater.from(this);        View alertView = layoutInflater.inflate(R.layout.dialog_edit_message, null);        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);        logDataSource.open();        //Sets the layout to the alertview        alertBuilder.setView(alertView);        final EditText editTextUserNote = (EditText) alertView.findViewById(R.id.editTextDialogUserInput);        editTextUserNote.setText(logDataSource.getNote(rowId));        alertBuilder.setCancelable(false)                .setTitle("Edit Note")                .setPositiveButton("Done", new DialogInterface.OnClickListener() {                    @Override                    public void onClick(DialogInterface dialogInterface, int i) {                        //Save the message to the database                        logDataSource.open();                        logDataSource.editNote(editTextUserNote.getText().toString(), rowId);                        logDataSource.close();                        dialogInterface.cancel();                        noteChanged = true;                        setResult = true;                        textNote.setText(editTextUserNote.getText().toString());                    }                }).setNegativeButton("Discard", new DialogInterface.OnClickListener() {            @Override            public void onClick(DialogInterface dialogInterface, int i) {                dialogInterface.cancel();            }        });        AlertDialog alertDialog = alertBuilder.create();        alertDialog.show();    }    private void deleteRecord() {        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);        alertBuilder.setCancelable(false)                .setMessage("Are you sure you want to delete this record?")                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {                    @Override                    public void onClick(DialogInterface dialogInterface, int i) {                        logDataSource.open();                        logDataSource.deleteRecord(rowId);                        //This activity has to pass a result back if a record has been deleted                        //so that the list view on the previous activity is able to be refreshed.                        setResult = true;                        deleteRecord = true;                        finish();                    }                })                .setNegativeButton("No", new DialogInterface.OnClickListener() {                    @Override                    public void onClick(DialogInterface dialogInterface, int i) {                        dialogInterface.cancel();                    }                });        AlertDialog alertDialog = alertBuilder.create();        alertDialog.show();    }}