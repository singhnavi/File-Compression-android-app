package com.example.ayush.comepressme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.util.zip.*;

public class MainActivity extends Activity implements OnClickListener {

    private static final int REQUEST_PICK_FILE = 1;

    private TextView filePath;
    private Button Browse;
    private File selectedFile;
    String inputPath;
    String inputFile;
    String outputPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filePath = (TextView)findViewById(R.id.file_path);
        Browse = (Button)findViewById(R.id.browse);
        Browse.setOnClickListener(this);



        ((Button) findViewById(R.id.button_zip))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        try {
                            inputPath = selectedFile.getPath();

                            System.out.println(inputPath);

                            char inPath[] = new char[100];

                            int length = inputPath.length();
                            inputPath.getChars(0, length, inPath, 0);
                            int j = length - 1, k = length - 1;
                            while (inPath[j] != '/') {
                                j--;
                            }
                            while (inPath[k] != '.') {
                                k--;
                            }
                            int fnlength = k - j - 1;
                            int extlength = length - k - 1;
                            char filename[] = new char[fnlength];
                            char extname[] = new char[extlength];
                            inputPath.getChars(j + 1, length - extlength - 1, filename, 0);
                            inputPath.getChars(k + 1, length, extname, 0);
                            String zipfn = new String(filename);
                            String zipext = new String(extname);
                            String newzipfn = new String();
                            newzipfn = zipfn + '(' + zipext + ')' + ".zip";

                            String newFolder = "/Zipdemo";
                            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                            File myNewFolder = new File(extStorageDirectory + newFolder);
                            myNewFolder.mkdir();

                            System.out.println(newzipfn);


                            FileInputStream fin = new FileInputStream(inputPath);
                            FileOutputStream fout = new FileOutputStream(myNewFolder.toString() + '/' + newzipfn);
                            DeflaterOutputStream out = new DeflaterOutputStream(fout);

                            int i;
                            while ((i = fin.read()) != -1) {
                                out.write((byte) i);
                                out.flush();
                            }

                            fin.close();
                            out.close();
                            Toast.makeText(MainActivity.this,"File Succesfully Compressed",Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                        System.out.println("rest of the code");
                    }

                });

        ((Button) findViewById(R.id.button_unzip))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        try{

                        inputPath = selectedFile.getPath();
                        System.out.println(inputPath);

                        char inPath[] = new char[100];

                        int length = inputPath.length();
                        inputPath.getChars(0, length, inPath, 0);
                        int j = length - 1,k=length-1,l=length-1;
                        while (inPath[j] != '(') {
                            j--;
                        }
                        while(inPath[k]!=')')
                        {
                            k--;
                        }
                        while(inPath[l]!='/')
                        {
                            l--;
                        }
                        int fnlength = j-l-1;
                        int extlength = k-j-1;
                        char filename[] =  new char[fnlength];
                        char extname[] = new char[extlength];
                        inputPath.getChars(l + 1, j, filename, 0);
                        inputPath.getChars(j + 1, k , extname, 0);
                        String unzipfn = new String(filename);
                        String unzipext = new String(extname);
                        String newunzipfn = new String();
                        newunzipfn = unzipfn + '.' + unzipext;

                        String newFolder = "/Unzipdemo";
                        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                        File myNewFolder = new File(extStorageDirectory + newFolder);
                        myNewFolder.mkdir();

                        System.out.println(newunzipfn);



                            FileInputStream fin=new FileInputStream(inputPath);
                            InflaterInputStream in=new InflaterInputStream(fin);

                            FileOutputStream fout=new FileOutputStream(myNewFolder.toString() + '/' + newunzipfn);

                            int i;
                            while((i=in.read())!=-1){
                                fout.write((byte)i);
                                fout.flush();
                            }

                            fin.close();
                            fout.close();
                            in.close();
                            Toast.makeText(MainActivity.this,"File Succesfully Uncompressed",Toast.LENGTH_SHORT).show();
                        }catch(Exception e){System.out.println(e);}

                        System.out.println("rest of the code");
                    }
                });


    }

    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.browse:
                Intent intent = new Intent(this, FilePicker.class);
                startActivityForResult(intent, REQUEST_PICK_FILE);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {
            switch (requestCode) {

                case REQUEST_PICK_FILE:

                    if (data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {

                        selectedFile = new File
                                (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        filePath.setText(selectedFile.getPath());
                    }
                    break;
            }
        }
    }

}