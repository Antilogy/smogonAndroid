package com.example.spielberg.smogonandroid;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Spielberg on 3/1/2018.
 */

public class SearchEngine implements Runnable {
    private Handler mHandler;
    private TableLayout table;
    private int start, end;
    private SearchSettings settings;

    public SearchEngine(Handler hand, TableLayout table,
                        int start, int end, SearchSettings settings){
        mHandler = hand;
        this.table = table;
        this.start = start;
        this.end = end;
        this.settings = settings;
    }


    @Override
    public void run(){


        // Moves the current Thread into the Background
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        /**
         * Code you want to run on the thread goes here
         */
        applySettings(table);
        //report when done
        Message message = mHandler.obtainMessage(1, "Done");
        message.sendToTarget();

    }

    public void applySettings(TableLayout v){

        for(int i=start;i<end;i++){
            TableRow row = (TableRow) v.getChildAt(i);
            applyName(row, i);
            applyType1(row, i);
            applyType2(row, i);
            applyAbility(row, i);
        }

    }

    private void applyAbility(TableRow row, int rowindex) {
        String text =((TextView) row.getChildAt(0)).getText().toString();
        int index = Integer.parseInt(text)-1;
        JSONArray abilitylist;
        String mytype;
        //only apply visible if type1 is an actual type
        try{
            if(!(settings.getAbility().compareTo("Ability")==0)){
                abilitylist = settings.getPokelist().getJSONObject(index).getJSONArray(
                        "alts").getJSONObject(
                        0).getJSONArray("abilities");
                for(int i=0;i<abilitylist.length();i++){
                    mytype = abilitylist.getString(i);
                    if(mytype.contains(settings.getAbility())){
                        return;
                    }

                }
                settings.setView(rowindex, View.GONE);



            }

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void applyType2(TableRow row, int rowindex) {
        String type =((TextView) row.getChildAt(3)).getText().toString();
        //only apply visible if type1 is an actual type
        if(!(settings.getType2().compareTo("Type2")==0)){
            if(!type.contains(settings.getType2())){
                settings.setView(rowindex, View.GONE);
            }
        }
    }

    private void applyType1(TableRow row, int rowindex) {
        String type =((TextView) row.getChildAt(3)).getText().toString();
        //only apply visible if type1 is an actual type
        if(!(settings.getType1().compareTo("Type1")==0)){
            if(!type.contains(settings.getType1())){
                settings.setView(rowindex, View.GONE);
            }
        }
    }

    public void applyName(TableRow row, int rowindex){
        String name =((TextView) row.getChildAt(2)).getText().toString();
        if(settings.getPokemon()==""){
            settings.setView(rowindex, View.VISIBLE);
        }
        else if(!name.toLowerCase().contains(settings.getPokemon())){
            settings.setView(rowindex, View.GONE);
        }
        else if(name.toLowerCase().contains(settings.getPokemon())){
            settings.setView(rowindex, View.VISIBLE);
        }
    }
}
