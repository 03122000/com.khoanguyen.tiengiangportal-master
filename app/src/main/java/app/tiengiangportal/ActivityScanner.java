package app.tiengiangportal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.tiengiangportal.data.DatabaseHandler;
import app.tiengiangportal.model.Place;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static app.tiengiangportal.ActivityMaps.EXTRA_OBJ;


public class ActivityScanner extends AppCompatActivity  implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    public DatabaseHandler db;
    public Place p = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        mScannerView= new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(mScannerView);
        mScannerView.startCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result result) {
        try {
            showResult(result);
        }catch (Exception e)
        {
            System.out.println("Ket qua quet barcode: "+e.toString());
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    public void showResult(final Result result)
    {
        String[] place;
        place=result.getText().split(" ");
        int nid = Integer.parseInt(place[0]);
        db = new DatabaseHandler(ActivityScanner.this);
        try {
            p = db.getPlace(nid);
        }catch (Exception e)
        {
            // return null.....
        }
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        String title;
        String messageResult;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        if ((Long.parseLong(timeStamp))>(Long.parseLong(place[2]))){
            Toast.makeText(this,"Hết hạn sử dụng",Toast.LENGTH_LONG).show();
        }
        else{
        if (p!= null)
        {
            title = "Chúc mừng bạn!";
            messageResult = "Thực phẩm bạn mua có nguồn gốc từ: "+p.name;
            b.setTitle(title);
            b.setMessage(messageResult+". \nBạn có muốn xem thêm thông tin địa điểm sản xuất thực phẩm?");
            b.setPositiveButton("Có", new DialogInterface. OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    try {
                        Intent intent = new Intent(getApplicationContext(),ActivityPlaceDetail.class);
                        intent.putExtra(EXTRA_OBJ, p);
                        startActivity(intent);
                    }catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
            b.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    //dialog.cancel();
                    finish();
                }
            });
        }
        else
        {
            title="Cảnh báo!";
            b.setTitle(title);
            messageResult="Không thể truy xuất được nguồn gốc thực phẩm này, xin vui lòng kiểm tra lại hoặc liên hệ với cửa hàng phân phối.";
            b.setMessage(messageResult+" \nBạn có muốn kiểm tra lại?");
            b.setNegativeButton("Thử lại", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int i) {
                     //dialogInterface.cancel();
                     finish();
                     startActivity(getIntent());
                 }
             });
            b.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    //dialog.cancel();
                    finish();
                }
            });
        }
        b.create().show();
    }}
}
