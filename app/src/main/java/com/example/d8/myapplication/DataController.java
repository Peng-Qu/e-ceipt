package com.example.d8.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


//this class handles some of the actions which may reuse many time
public class DataController {
    //add new receipt data to local json file
    static void addReceiptToLocal(Receipt receipt, Context ctx)throws JSONException{
        String receiptsJSON = DataController.readJsonFile(Information.RECEIPTSLOCALFILENAME, ctx);

        JSONArray receiptsJsonArray = new JSONArray(receiptsJSON);
        JSONObject jsonObject = new JSONObject();
        JSONArray itemsJsonArray = new JSONArray();
        JSONObject itemJsonObject = new JSONObject();
        //itemsJsonArray.put(itemJsonObject);

        String company = receipt.getBusinessName();
        String date = receipt.getDate();
        String username = Information.authUser.getName();
        String userId = Information.authUser.getUserId();
        String tCost = Double.toString(receipt.getTotalCost());
        String tax = "14";

        jsonObject.put("name", username);
        jsonObject.put("receiptID", "-1");
        jsonObject.put("date", date);
        jsonObject.put("totalCost", tCost);
        jsonObject.put("tax", tax);
        jsonObject.put("businessName", company);
        jsonObject.put("items",itemsJsonArray);

        String jsonString = jsonObject.toString();

        Log.i("JSONINAddReceiptForm:", jsonString);

        receiptsJsonArray.put(jsonObject);

        String jArrayString = receiptsJsonArray.toString();
        Log.i("JArrAddReceiptForm:", jArrayString);

        DataController.storeJsonToLocal(jArrayString, Information.RECEIPTSLOCALFILENAME, ctx);
    }

    //add new receipt data to remote databse
    static void addReceiptToDB(Receipt receipt){

    }

    //Store json string to mobile local file
    public static void storeJsonToLocal(String json, String filename, Context ctx) throws JSONException {
        //String username = Information.user.getUserName();
        //String filename = "_receipts"+".txt";

        try {
            FileOutputStream outputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("STOREERROR:", e.toString());
        }
    }

    //read json from local file
    public static String readJsonFile(String filename, Context ctx){
        //String username = Information.user.getUserName();
        //String filename = "_receipts"+".txt";
        String json = "";
        try{
            FileInputStream inputStream = ctx.openFileInput(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer);
            //Toast.makeText(getApplicationContext(),json,Toast.LENGTH_LONG).show();
            return json;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //code tutorial from "https://www.simplifiedcoding.net/android-json-parsing-tutorial/"
    //this method is to syncronize remote DB data to mobile local storage.
    public static String SyncronizeData(final String urlWebService, Context ctx) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */

        //The local file which store receipts data
        String RECEIPTDATAFILE = "_receipts.txt";

        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);

            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {

                try {
//                    //creating a URL
//                    URL url = new URL(urlWebService);
//
//                    //Opening the URL using HttpURLConnection
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//                    con.setRequestMethod("GET");
//                    con.connect();
//                    //StringBuilder object to read the string from the service
//                    StringBuilder sb = new StringBuilder();
//
//                    //We will use a buffered reader to read the string from service
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                    //A simple string to read values from each line
//                    String json;
//
//                    //reading until we don't find null
//                    while ((json = bufferedReader.readLine()) != null) {
//                        Log.d("JSONARRAY", json);
//                        //appending it to string builder
//                        sb.append(json + "\n");
//                    }
//
//                    String jsonReturn = sb.toString().trim();
//
//                    Log.i("JSONRETURN", jsonReturn);
//                    storeJsonToLocal(jsonReturn, RECEIPTDATAFILE, ctx);
//                    //storeJsonToLocal(jsonReturn);
//
//                    //finally returning the read string
//                    return sb.toString().trim();


                    //Post version
                    //creating a URL
                    URL url = new URL(urlWebService);
                    HttpURLConnection conn = null;

                    //String username = Information.authUser.getName();
                    String username = "John Doe";
                    Log.i("USERNAME3333", username);


                    //open connection
                    conn = (HttpURLConnection) url.openConnection();

                    //set the request method to post
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    //prepare data to post
                    //List<NameValuePair> params = new ArrayList<NameValuePair>();
                    List<AbstractMap.SimpleEntry> params = new ArrayList<AbstractMap.SimpleEntry>();

//                  params.add(new BasicNameValuePair("firstParam", paramValue1));
//                  params.add(new BasicNameValuePair("secondParam", paramValue2));
//                  params.add(new BasicNameValuePair("thirdParam", paramValue3));
                    JSONObject newJson = new JSONObject();
                    newJson.put("name", username);

                    String message = newJson.toString();

                    //Output the stream to the server
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(message);
                    writer.flush();
                    writer.close();
                    os.close();

                    conn.connect();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        Log.d("JSONARRAY", json);
                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    String jsonReturn = sb.toString().trim();

                    Log.i("JSONRETURN", jsonReturn);
                    storeJsonToLocal(jsonReturn, RECEIPTDATAFILE, ctx);
                    //storeJsonToLocal(jsonReturn);

                    //finally returning the read string
                    return sb.toString().trim();

//                    }catch(MalformedURLException error) {
//                        //Handles an incorrectly entered URL
//                    }
//                    catch(SocketTimeoutException error) {
//                        //Handles URL access timeout.
//                    }
//                    catch (IOException error) {
//                        //Handles input and output errors
//                    }finally {
//                        if(conn != null) // Make sure the connection is not null.
//                            conn.disconnect();
//                    }


                } catch (Exception e) {
                    Log.i("FAIL222",e.toString());
                    return null;
                }
            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        String result="";
        try{
            result = getJSON.execute().get();
        }catch (Exception e){

        }

        return result;
    }

    //this method is to load json format receipts data to "Information.receipts" object
    public static void loadReceiptsObj(String json) throws JSONException {
        try{
            JSONArray jsonArray = new JSONArray(json);
            Receipt receipt;

            Log.i("JSONOOOOOO",json);
            Log.i("JSONLENGHT", Integer.toString(jsonArray.length()));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                receipt =  new Receipt();
                Log.d("RECEIPTname:", obj.getString("name"));
                receipt.setName(obj.getString("name"));
                Log.d("RECEIPTid:", obj.getString("receiptID"));
                receipt.setReceipId(obj.getString("receiptID"));
                Log.d("RECEIPTdate:", obj.getString("date"));
                receipt.setDate(obj.getString("date"));
                Log.d("RECEIPTtotalcost:", obj.getString("totalCost"));
                receipt.setTotalCost(Double.parseDouble(obj.getString("totalCost")));
                Log.d("RECEIPTtax:", obj.getString("tax"));
                receipt.setTax(Double.parseDouble(obj.getString("tax")));
                Log.d("RECEIPTbusiness:", obj.getString("businessName"));
                receipt.setBusinessName(obj.getString("businessName"));

                Log.d("RECEIPTOBJ", "name:"+receipt.getName()+
                        "id:"+receipt.getReceipId()+
                        "date:"+receipt.getDate()+
                        "totalCost:"+receipt.getTotalCost()+receipt.getTax());

                JSONArray itemarray = obj.getJSONArray("items");

                Log.d("ITEMARRAYL:",itemarray.toString());

                for(int j=0; j<itemarray.length();j++){
                    JSONObject itemObj = itemarray.getJSONObject(j);

                    Log.d("ITEMNAME22222", itemObj.getString("itemName"));
                    Log.d("ITEMDECS", itemObj.getString("itemDesc"));
                    Log.d("ITEMPRICE", itemObj.getString("itemPrice"));

                    receipt.addItem(itemObj.getString("itemName"), itemObj.getString("itemDesc"), Double.parseDouble(itemObj.getString("itemPrice")));
                }

                Information.receipts.add(receipt);

            }
        }catch(Exception e){
            Log.e("ERRRRR:",e.toString());
        }

        if(!Information.receipts.get(0).getItems().isEmpty()){
            Log.d("RECEIPTOBJ2:",Information.receipts.get(0).getItems().get(1).getItemName());

        }
    }

    //This method is to parse json format string to an Receipt object and return Receipt object
    public static Receipt parseJsonToReceiptOBJ(String json)throws JSONException{
        JSONObject obj = new JSONObject(json);
        Receipt receipt =  new Receipt();
        Log.d("RECEIPTname:", obj.getString("name"));
        receipt.setName(obj.getString("name"));
        Log.d("RECEIPTid:", obj.getString("receiptID"));
        receipt.setReceipId(obj.getString("receiptID"));
        Log.d("RECEIPTdate:", obj.getString("date"));
        receipt.setDate(obj.getString("date"));
        Log.d("RECEIPTtotalcost:", obj.getString("totalCost"));
        receipt.setTotalCost(Double.parseDouble(obj.getString("totalCost")));
        Log.d("RECEIPTtax:", obj.getString("tax"));
        receipt.setTax(Double.parseDouble(obj.getString("tax")));
        Log.d("RECEIPTbusiness:", obj.getString("businessName"));
        receipt.setBusinessName(obj.getString("businessName"));

        Log.d("RECEIPTOBJ", "name:"+receipt.getName()+
                "id:"+receipt.getReceipId()+
                "date:"+receipt.getDate()+
                "totalCost:"+receipt.getTotalCost()+receipt.getTax());

        JSONArray itemarray = obj.getJSONArray("items");

        Log.d("ITEMARRAYL:",itemarray.toString());

        for(int j=0; j<itemarray.length();j++){
            JSONObject itemObj = itemarray.getJSONObject(j);

            receipt.addItem(itemObj.getString("itemName"), itemObj.getString("itemDesc"), Double.parseDouble(itemObj.getString("itemPrice")));
        }
        return receipt;
    }

    //this metod is to get receipts in specific days, like 3 days, 7 days...
    public static ArrayList<Receipt> getReceiptsInDays(Integer days){
        ArrayList<Receipt> receipts = new ArrayList<Receipt>();
        try{
            Date currentDate = DataController.getCurrentDate();

            try{

                for(int i=0; i<Information.receipts.size(); i++){
                    Integer days_ = DataController.dateDiff(DataController.parseStringToDate(Information.receipts.get(i).getDate()),currentDate);
                    if(days_<=days){
                        Log.i("RECEIPT["+i+"]",Information.receipts.get(i).toString());
                        receipts.add(Information.receipts.get(i));
                    }
                }
            }catch(Exception e){
                Log.i("DAYSFAIL", e.toString());
            }

        }catch(Exception e){

        }

        return receipts;
    }

    //method to get current date
    public static Date getCurrentDate()throws Exception{

        //get current date and time
        Calendar calender = Calendar.getInstance();
        int cDay = calender.get(Calendar.DAY_OF_MONTH);
        int cMonth = calender.get(Calendar.MONTH) + 1;
        int cYear = calender.get(Calendar.YEAR);
        int cHour = calender.get(Calendar.HOUR);
        int cMinute = calender.get(Calendar.MINUTE);
        int cSecond = calender.get(Calendar.SECOND);

        //String cDateTime = ""+cDay+"/"+cMonth+"/"+cYear+" "+cHour+":"+cMinute+":"+cSecond;
        //String cDateInString = ""+cYear+"-"+cMonth+"-"+cDay;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        String cDateInString = sdf.format(calender.getTime()).toString();
        Date currentDate = sdf.parse(cDateInString);

        return currentDate;
    }

    //this method is to get two date difference
    public static Integer dateDiff(Date startDate, Date endDate){
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String resultString = ""+elapsedDays;

        Integer result = Integer.parseInt(resultString);
        return result;
    }

    public static Date parseStringToDate(String dateInString)throws Exception{


        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        //String cDateInString = sdf.format(dateInString).toString();
        Date currentDate = sdf.parse(dateInString);
        return currentDate;
    }


}
