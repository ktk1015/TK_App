package com.test.tk.tk_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button saveBtn, getMeminfoBtn,delMemdataBtn;
    Context context;
    private final String urlPath = "http://ktk1015.dothome.co.kr/s2";
    private final String TAG = "tk_test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        saveBtn = (Button)findViewById(R.id.save_btn);
        getMeminfoBtn = (Button)findViewById(R.id.getMemberInfo_btn);
        delMemdataBtn = (Button)findViewById(R.id.delMemData_btn);
        saveBtn.setOnClickListener(this);
        getMeminfoBtn.setOnClickListener(this);
        delMemdataBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_btn:
                Log.d("tk_test", "save btn!!");
                Toast.makeText(getApplicationContext(), "save btn!!!", Toast.LENGTH_SHORT).show();
                HttpConnection thread = new HttpConnection();
                thread.execute();
                break;
            case R.id.getMemberInfo_btn:
                Toast.makeText(getApplicationContext(), "get meminfo btn!!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delMemData_btn:
                Toast.makeText(getApplicationContext(),"del Memdata btn!!!",Toast.LENGTH_SHORT).show();
                DelMemDataThread delThread = new DelMemDataThread();
                delThread.execute();
            default:
                break;
        }
    }//onClick()

    private class HttpConnection extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urlParams) {
            URL url;
            //url = urlParams[0];
            String serverMsg = null;

            try {
                //url = new URL("http://ktk1015.dothome.co.kr/s2/test_tk.php");   //URL클래스의 인스턴스 생성
                url = new URL("http://ktk1015.dothome.co.kr/s2/inputMemdata.php");   //URL클래스의 인스턴스 생성

                // 해당 주소의 페이지로 접속을 하고, 단일 HTTP 접속을 하기위해 캐스트한다.
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if(urlConnection != null)
                {
                    /***************************************
                     전송모드 설정
                     ****************************************/
                    urlConnection.setRequestMethod("POST");     // 아무것도 설정 안 했을 경우 default 값 = GET
                    urlConnection.setConnectTimeout(3000);
                    urlConnection.setDoInput(true);             // InputStream으로 서버로 부터 응답 헤더와 메시지를 읽어들이겠다는 옵션
                    urlConnection.setDoOutput(true);            // OutputStream으로 데이터를 서버로 넘겨주겠다는 옵션
                    urlConnection.setUseCaches(true);           // Cache 사용안함
                    //"Content-Type" 프로퍼티를 "application/x-www-form-urlencoded"로 변경해주는 것을 알 수 있는 데,
                    // 이렇게 함으로써 웹 서버는 POST 방식의 데이터가 인코딩된 데이터라는 것을 알 수 있게 된다.
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //urlConnection.setRequestProperty("Content-Type", "application/json");

                    /***************************************
                     서버로 Data 전송
                     ****************************************/
                    SendMsgDefault(urlConnection);

                    /*
                    Log.d(TAG, "connection is OK");
                    //JSONObject 사용하여 서버에 보낼 Data 생성
                    JSONObject jobj = new JSONObject();
                    jobj.put("memid", "ktk1010");
                    jobj.put("age", "32");

                    JSONArray jArray = new JSONArray();
                    jArray.put(jobj);

                    String temp111 = jobj.toString();
                    String temp222 = jArray.toString();
                    Log.d(TAG, "temp111: " + temp111);
                    Log.d(TAG, "temp222: " + temp222);

                    String toServerData = temp111;
                    OutputStream os = urlConnection.getOutputStream();
                    // write메소드로 메시지로 작성된 파라미터정보를 바이트단위로 "EUC-KR"로 인코딩해서 요청한다.
                    // 여기서 중요한 점은 "UTF-8"로 해도 되는데 한글일 경우는 "EUC-KR"로 인코딩해야만 한글이 제대로 전달된다.
                    os.write(toServerData.getBytes("UTF-8"));
                    os.flush(); // 스트림 버퍼 비우기
                    os.close();
                    */
                    //response
                    if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                    {
                        /***************************************
                         서버 msg 읽기
                         ****************************************/
                        serverMsg = ReadServerResult(urlConnection);
                        Log.d("tk_test", "from server = " + serverMsg);
                    }
                    else
                    {
                        Log.d(TAG,"connection is fail");
                    }

                }
                else
                {
                    //urlConnection == null
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }//doInBackground

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();
        }
    }//HttpConnection()

    private class DelMemDataThread extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL url;
            String serverMsg=null;

            try {
                url=new URL("http://ktk1015.dothome.co.kr/s2/delMemdata.php");  //URL클래스의 인스턴스 생성

                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();

                if(urlConnection !=null)
                {
                    /***************************************
                     전송모드 설정
                     ****************************************/
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setConnectTimeout(3000);
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.setUseCaches(true);
                    urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                    /***************************************
                     서버로 Data 전송
                     ****************************************/
                    SendMsgDefault(urlConnection);

                    //response
                    if(urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK)
                    {
                        /***************************************
                         서버 msg 읽기
                         ****************************************/

                        serverMsg = ReadServerResult(urlConnection);
                        Log.d("tk_test","End from server ="+serverMsg);
                        /*
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
                        StringBuilder builder = new StringBuilder();
                        String str;

                        while((str=in.readLine())!=null)
                        {
                            builder.append(str);
                        }

                        in.close();
                        serverMsg = builder.toString();
                        Log.d("tk_test","from server ="+serverMsg);
                        */
                    }
                    else
                    {
                        Log.d(TAG,"connection is fail");
                    }

                }


            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }//doInBackground

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }

    /***************************************
     함수명: ReadServerResult
     기능: 서버로부터의 결과값 Read
     ****************************************/
    private String ReadServerResult(HttpURLConnection urlConnection) throws IOException {
        String result="";

        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
        StringBuilder builder = new StringBuilder();
        String str;

        while((str=in.readLine())!=null)
        {
            builder.append(str);
        }

        in.close();
        result = builder.toString();
        Log.d("tk_test","from server ="+result);

        return result;
    }

    /***************************************
     함수명: SendMsgDefault
     기능: 서버로 Data 전송
     ****************************************/
    private void SendMsgDefault(HttpURLConnection urlConnection)
    {
        try {
            //전송할 데이터
            String cMemid = "ktk66666";
            int cAge = 11;
            String cMsg = "kkjajakek";      //전송할 msg
            String toServerData ="";

            //전송 String 생성
            StringBuffer buffer = new StringBuffer();
            buffer.append("memid").append("=").append(cMemid).append("&");  //php변수에 값대입
            buffer.append("age").append("=").append(cAge).append("&");      //변수구분은 & 사용
            buffer.append("msg").append("=").append(cMsg);
            toServerData = buffer.toString();
            Log.d(TAG, "toServerData= "+toServerData);

            OutputStream os = urlConnection.getOutputStream();
            // write메소드로 메시지로 작성된 파라미터정보를 바이트단위로 "EUC-KR"로 인코딩해서 요청한다.
            // 여기서 중요한 점은 "UTF-8"로 해도 되는데 한글일 경우는 "EUC-KR"로 인코딩해야만 한글이 제대로 전달된다.
            os.write(toServerData.getBytes("UTF-8"));
            os.flush(); // 스트림 버퍼 비우기
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }//SendMsgDefault()
}
