package com.avaygo.myfastingtracker.activities;import android.app.Activity;import android.content.Intent;import android.os.Bundle;import android.view.Menu;import android.view.MenuItem;import android.widget.TextView;import com.avaygo.myfastingtracker.R;import java.text.SimpleDateFormat;import java.util.Calendar;import java.util.Locale;public class FastDetailsActivity extends Activity {    SimpleDateFormat titleFormat = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault());    SimpleDateFormat dayDateMonthFormat = new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault());    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_fast_details);        final Intent intent = getIntent();        Calendar recordDate = Calendar.getInstance(); // The date that the fast was logged in to the database        Calendar startDate = Calendar.getInstance(); // The date that the fast started on        Calendar endDate = Calendar.getInstance(); // The date that the fast was intended to end on        //Get the intent extras that are to be used later on in the body text        int fastDuration = intent.getIntExtra("FAST_DURATION", 0);        int percentComplete = intent.getIntExtra("PERCENT_COMPLETE", 0);        recordDate.setTimeInMillis(intent.getLongExtra("LOG_TIMESTAMP", 0));        startDate.setTimeInMillis(intent.getLongExtra("START_TIMESTAMP", 0));        endDate.setTimeInMillis(intent.getLongExtra("END_TIMESTAMP", 0));        TextView textEndTitle = (TextView) findViewById(R.id.text_end_title);        textEndTitle.setText(titleFormat.format(recordDate.getTime()));        TextView textblock1 = (TextView) findViewById(R.id.text_block_1);        textblock1.setText("This fast started at " + timeFormat.format(startDate.getTime()) + " on " +                dayDateMonthFormat.format(startDate.getTime()) + " and ended at " + timeFormat.format(recordDate.getTime())                + " on " + dayDateMonthFormat.format(recordDate.getTime()) + ".");        TextView textblock2 = (TextView) findViewById(R.id.text_block_2);        //If the user has achieved 100% on the fast then it is unnecessary to show the intened finishing time as well.        if(percentComplete == 100){             textblock2.setText("It was intended to last for " + fastDuration + " hours.");        }else {            textblock2.setText("It was intended to last for " + fastDuration + " hours ending at " + timeFormat.format(endDate.getTime())                    + " on " + dayDateMonthFormat.format(endDate.getTime()) + ".");        }        TextView textblock3 = (TextView) findViewById(R.id.text_block_3);        if(percentComplete == 100){            if(fastDuration > 1) {                textblock3.setText("You achieved " + fastDuration + " hours. Well done!");            }            else{                textblock3.setText("You achieved " + fastDuration + " hour. Well done!");            }        }        else if (percentComplete < 50){        //How long they had left            long timeLeft = endDate.getTimeInMillis() - recordDate.getTimeInMillis();            textblock3.setText("You completed " +percentComplete+ "% and had " + longToHours(timeLeft) + " and " + longToMins(timeLeft) +                    " remaining.");        }        else if (percentComplete < 100 && percentComplete >= 50){            //How long they achieved            long timeCompleted = recordDate.getTimeInMillis() - startDate.getTimeInMillis();            textblock3.setText("You achieved " + longToHours(timeCompleted) + " and " + longToMins(timeCompleted) +            " for " +percentComplete+"% completion." );        }        TextView textNote = (TextView) findViewById(R.id.text_note);        textNote.setText(intent.getStringExtra("USER_NOTE"));    }    public String longToHours(long millisToCalculate){        int iHours = (int) ((millisToCalculate/ (1000 * 60 * 60)) % 24);        int totalMinutes = (int) (millisToCalculate / 1000);        String hours;        String length;            hours = Integer.toString(iHours);        if(iHours == 1){        length =  hours + " hour";        }        else {           length = hours + " hours";        }        //Fasts less than an hour don't get recorded        return length;    }    public String longToMins(long millisToCalculate){        int iMinutes = (int) ((millisToCalculate / (1000 * 60)) % 60);        int totalMinutes = (int) (millisToCalculate / 1000);        String minutes;        String length;        //Formating code, add leading zero if less than 10 minutes        if (iMinutes < 10) {            minutes = ("0" + Integer.toString(iMinutes));        } else {            minutes = Integer.toString(iMinutes);        }        if(iMinutes == 1){            length =  minutes + " minute";        }        else {            length = minutes + " minutes";        }        return length;    }    @Override    public boolean onCreateOptionsMenu(Menu menu) {        // Inflate the menu; this adds items to the action bar if it is present.        getMenuInflater().inflate(R.menu.menu_fast_details, menu);        return true;    }    @Override    public boolean onOptionsItemSelected(MenuItem item) {        // Handle action bar item clicks here. The action bar will        // automatically handle clicks on the Home/Up button, so long        // as you specify a parent activity in AndroidManifest.xml.        int id = item.getItemId();        //noinspection SimplifiableIfStatement        if (id == R.id.action_settings) {            return true;        }        return super.onOptionsItemSelected(item);    }}