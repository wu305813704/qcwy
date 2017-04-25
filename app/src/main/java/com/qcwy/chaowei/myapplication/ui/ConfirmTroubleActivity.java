package com.qcwy.chaowei.myapplication.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.qcwy.chaowei.myapplication.R;
import com.qcwy.chaowei.myapplication.adapter.FaultPartAdapter;
import com.qcwy.chaowei.myapplication.adapter.TroublePickerAdapter;
import com.qcwy.chaowei.myapplication.app.MyApplication;
import com.qcwy.chaowei.myapplication.entity.JsonResult;
import com.qcwy.chaowei.myapplication.entity.PartDetail;
import com.qcwy.chaowei.myapplication.entity.response.ResponsePartDetail;
import com.qcwy.chaowei.myapplication.utils.FaultUtils;
import com.qcwy.chaowei.myapplication.utils.GlobalContants;
import com.qcwy.chaowei.myapplication.utils.MyItemDecoration;
import com.qcwy.chaowei.myapplication.utils.MyToast;
import com.qcwy.chaowei.myapplication.utils.Urls;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPagerActivity;
import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.utils.PhotoPickerIntent;
import okhttp3.Call;
import okhttp3.Response;

@ContentView(R.layout.activity_confirm_trouble)
public class ConfirmTroubleActivity extends BaseActivity {
    //添加零件
    @ViewInject(R.id.btn_add_part)
    private Button btnAddPart;
    @ViewInject(R.id.sp_fault)
    private Spinner spFault;
    private List<String> spList;
    private ArrayAdapter<String> spAdapter;
    private int faultId = 0;
    @ViewInject(R.id.et_description)
    private EditText etDescription;
    //发送给客户
    @ViewInject(R.id.btn_send)
    private Button btnSend;


    @ViewInject(R.id.ib_add_images)
    private ImageButton ibAddImages;
    //选取图片存放位置
    @ViewInject(R.id.gv_images)
    private GridView gv;
    //添加零件
    @ViewInject(R.id.lv_part)
    private RecyclerView rvPart;
    private FaultPartAdapter faultPartAdapter;
    private List<PartDetail> parts;
    private List<Integer> count;

    //照片地址集合
    private List<String> photos;
    private TroublePickerAdapter adapterImage;
    private static final int REQUEST_CODE_PICK = 0;
    private static final int REQUEST_CODE_PREVIER = 1;
    //已选择图片数
    private int imageNumber = 0;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("确认故障");
        spFault.setAdapter(spAdapter);
        rvPart.setAdapter(faultPartAdapter);
    }

    @Override
    protected void init() {
        gson = new Gson();
        photos = new ArrayList<>();
        adapterImage = new TroublePickerAdapter(this, photos);
        gv.setAdapter(adapterImage);
        //设置下拉选内容
        spList = new ArrayList<>();
        spList.add("请选择故障类型");
        for (int i = 1; i <= 22; i++) {
            spList.add(FaultUtils.parseApp(i));
        }
        spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spList);
        parts = new ArrayList<>();
        count = new ArrayList<>();
        //设置布局格式
        rvPart.setLayoutManager(new LinearLayoutManager(this));
        faultPartAdapter = new FaultPartAdapter(this, parts, count);

        //RecycleView添加分割线
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPart.addItemDecoration(new MyItemDecoration(this, layoutManager.getOrientation()));
    }

    @Override
    protected void setListeners() {
        ibAddImages.setOnClickListener(this);
        btnAddPart.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ConfirmTroubleActivity.this, PhotoPagerActivity.class);
                intent.putExtra(PhotoPagerActivity.EXTRA_CURRENT_ITEM, position);
                intent.putStringArrayListExtra(PhotoPagerActivity.EXTRA_PHOTOS, (ArrayList<String>) photos);
                startActivityForResult(intent, REQUEST_CODE_PREVIER);
            }
        });
        spFault.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                faultId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.iv_msg:
                //跳转到Message界面
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.ib_add_images:
                if (imageNumber == 3) {
                    MyToast.show(this, "你最多只能选择3张图片");
                    return;
                }
                //拍照或添加照片
                PhotoPickerIntent intent = new PhotoPickerIntent(this);
                //最多选取三张图片
                intent.setPhotoCount(3 - imageNumber);
                intent.setShowCamera(true);
                startActivityForResult(intent, REQUEST_CODE_PICK);
                break;
            case R.id.btn_add_part:
                //添加零件
                startActivityForResult(new Intent(this, AddPartActivity.class), GlobalContants.RESULT_CODE_ADD_PART);
                break;
            case R.id.btn_send:
                //发送给客户
                if (faultId == 0) {
                    MyToast.show(getApplicationContext(), "请选择故障类型");
                    return;
                }
                final String description = etDescription.getText().toString();
                final List<String> parts = new ArrayList<>();
                for (PartDetail partDetail : this.parts) {
                    parts.add(partDetail.getPart_detail_id() + "-" + partDetail.getCount() + "-" + partDetail.getType());
                }
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定提交故障给客户?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //提交故障信息
                                submit(String.valueOf(faultId), description, parts, photos);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                break;
        }
    }

    //提交故障信息
    private void submit(String faultId, String description, List<String> part, List<String> photos) {
        List<File> files = new ArrayList<>();
        for (String str : photos) {
            files.add(new File(str));
        }
        OkGo.post(Urls.CONFIRM_TROUBLE)          // 请求方式和请求url
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey(Urls.CONFIRM_TROUBLE)  // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                .isMultipart(true)               // 设置强制为文件上传
                .params("orderNo", MyApplication.getApp().getOrder().getOrder_no())
                .params("jobNo", MyApplication.jobNo)
                .params("faultId", faultId)
                .params("faultDescription", description)
                .addFileParams("files", files)
                .addUrlParams("parts", part)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        JsonResult result = gson.fromJson(s, JsonResult.class);
                        if (result.getState() == 0) {
                            MyToast.show(getApplicationContext(), "故障信息已发送，等待客户确认");
                            MyApplication.getApp().getOrder().setState(7);
                            startActivity(new Intent(getApplicationContext(), OrderDetailActivity.class));
                            finish();
                        } else {
                            MyToast.show(getApplicationContext(), result.getMessage());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        if (response == null) {
                            MyToast.show(getApplicationContext(), "网络连接失败!");
                        } else {
                            MyToast.show(getApplicationContext(), e);
                        }
                    }
                });
    }

    //照片选完后返回处理图片
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择图片
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK) {
            //选取图片路径集合
            ArrayList<String> photos =
                    data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
//            this.photos.clear();
            this.photos.addAll(photos);
            adapterImage.notifyDataSetChanged();
            imageNumber += photos.size();
        }
        //删除图片
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PREVIER) {
            //所有照片都删除时，data==null
            if (data == null) {
                this.photos.clear();
                adapterImage.notifyDataSetChanged();
                imageNumber = 0;
                return;
            }
            ArrayList<String> photos =
                    data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
            this.photos.clear();
            this.photos.addAll(photos);
            adapterImage.notifyDataSetChanged();
            imageNumber = this.photos.size();
        }

        //添加零件
        if (resultCode == RESULT_OK && requestCode == GlobalContants.RESULT_CODE_ADD_PART) {
            ArrayList<String> listParts = data.getStringArrayListExtra(GlobalContants.KEY_ADD_PART);
            final ArrayList<Integer> countParts = data.getIntegerArrayListExtra(GlobalContants.KEY_ADD_PART_COUNT);
            count.clear();
            count.addAll(countParts);
            if (listParts.isEmpty()) {
                return;
            }
            //根据id集合查询零件详情
            OkGo.get(Urls.GET_PARTS_BY_IDS)     // 请求方式和请求url
                    .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                    .cacheKey(Urls.GET_PARTS_BY_IDS)            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                    .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍
                    .addUrlParams("partIds", listParts)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            ResponsePartDetail result = gson.fromJson(s, ResponsePartDetail.class);
                            if (result.getState() == 0) {
                                parts.clear();
                                parts.addAll(result.getData());
                                faultPartAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            if (response == null) {
                                MyToast.show(getApplicationContext(), "网络连接失败!");
                            } else {
                                MyToast.show(getApplicationContext(), e);
                            }
                        }
                    });
        }
    }

}
