package com.twt.service.settings;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;
import com.twt.service.R;
import com.twt.service.home.HomeActivity;
import com.twt.service.update.UpdateManager;
import com.twt.wepeiyang.commons.cache.CacheProvider;
import com.twt.wepeiyang.commons.experimental.CommonPreferences;
import com.twt.wepeiyang.commons.network.RxErrorHandler;
import com.twtstudio.retrox.auth.api.AuthProvider;
import com.twtstudio.retrox.bike.service.BikeServiceProvider;
import com.twtstudio.retrox.tjulibrary.provider.TjuLibProvider;

import es.dmoral.toasty.Toasty;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by retrox on 2017/2/21.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle("偏好设置");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        CrashReport.testJavaCrash();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setmContext(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, fragment)
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static class SettingsFragment extends PreferenceFragment {

        private Activity mContext;

        public SettingsFragment() {
        }

        public void setmContext(Activity mContext) {
            this.mContext = mContext;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
            initPrefs();
        }

        @Override
        public void onResume() {
            super.onResume();
            Preference isBindLib = findPreference(getString(R.string.pref_is_bind_lib));
            isBindLib.setSummary(CommonPreferences.INSTANCE.isBindLibrary() ? "已绑定" : "未绑定");
        }

        public void initPrefs() {

            /**
             * 一键退学
             */

            Preference exitTjuPref = findPreference(getString(R.string.pref_is_exit_tju));
            int dropOutMode = CommonPreferences.INSTANCE.getDropOut();
            String dropOutSummary = "未操作";
            if (dropOutMode == 0) {
                dropOutSummary = "未操作";
            } else if (dropOutMode == 1) {
                dropOutSummary = "已退学";
            } else if (dropOutMode == 2) {
                dropOutSummary = "已复学";
            }
            exitTjuPref.setSummary("退学状态: " + dropOutSummary);

            /**
             * 这个代码有些冗杂 ... 因为一些内部类的调用关系没有理清楚但是先这样子吧
             */
            exitTjuPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {

                    if (CommonPreferences.INSTANCE.getDropOut() == 0 || CommonPreferences.INSTANCE.getDropOut() == 2) {
                        //退学
                        String[] items = {"我要打游戏！", "我要运动！", "我要睡觉！", "怎么样都好啦！"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                                .setTitle("你为啥要退学呀？")
                                .setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Toast.makeText(mContext, "正在办理...", Toast.LENGTH_SHORT).show();

                                        RealAuthApi.INSTANCE
                                                .dropOut(which + 1)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(it -> {
                                                    if (!TextUtils.isEmpty(it.getData())) {
                                                        Toast.makeText(mContext, it.getData(), Toast.LENGTH_SHORT).show();

                                                        if (it.getData().equals("欢迎回来上学 (〃∀〃)")) {
                                                            CommonPreferences.INSTANCE.setDropOut(2);
                                                        } else if (it.getData().equals("退学成功 d(`･∀･)b")) {
                                                            CommonPreferences.INSTANCE.setDropOut(1);
                                                        }

                                                        //刷新下状态 --
                                                        int dropOutMode = CommonPreferences.INSTANCE.getDropOut();
                                                        String dropOutSummary = "未操作";
                                                        if (dropOutMode == 0) {
                                                            dropOutSummary = "未操作";
                                                        } else if (dropOutMode == 1) {
                                                            dropOutSummary = "已退学";
                                                        } else if (dropOutMode == 2) {
                                                            dropOutSummary = "已复学";
                                                        }
                                                        exitTjuPref.setSummary("退学状态: " + dropOutSummary);

                                                        //清空缓存 --- 课程表 GPA
                                                        CacheProvider.clearCache();

                                                        AuthProvider.INSTANCE.authSelf(false, true); //null做了处理的 刷新状态
                                                    }
                                                    dialog.dismiss();
                                                }, throwable -> {
                                                    AuthProvider.INSTANCE.authSelf(false, true); //null做了处理的
                                                    new RxErrorHandler().call(throwable);
                                                    dialog.dismiss();
                                                });
                                    }
                                });

                        builder.create().show();

                    } else {
                        AlertDialog.Builder dropInBuilder = new AlertDialog.Builder(mContext)
                                .setTitle("复学申请办理处")
                                .setMessage("浪子回头金不换啊...")
                                .setPositiveButton("我想好了...", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(mContext, "正在办理...", Toast.LENGTH_SHORT).show();

                                        RealAuthApi.INSTANCE
                                                .dropOut(0)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(it -> {
                                                    if (!TextUtils.isEmpty(it.getData())) {
                                                        Toast.makeText(mContext, it.getData(), Toast.LENGTH_SHORT).show();

                                                        if (it.getData().equals("欢迎回来上学 (〃∀〃)")) {
                                                            CommonPreferences.INSTANCE.setDropOut(2);
                                                        } else if (it.getData().equals("退学成功 d(`･∀･)b")) {
                                                            CommonPreferences.INSTANCE.setDropOut(1);
                                                        }

                                                        //刷新下状态 --
                                                        int dropOutMode = CommonPreferences.INSTANCE.getDropOut();
                                                        String dropOutSummary = "未操作";
                                                        if (dropOutMode == 0) {
                                                            dropOutSummary = "未操作";
                                                        } else if (dropOutMode == 1) {
                                                            dropOutSummary = "已退学";
                                                        } else if (dropOutMode == 2) {
                                                            dropOutSummary = "已复学";
                                                        }
                                                        exitTjuPref.setSummary("退学状态: " + dropOutSummary);

                                                        //清空缓存 --- 课程表 GPA
                                                        CacheProvider.clearCache();

                                                        AuthProvider.INSTANCE.authSelf(false, true);//null做了处理的 刷新状态
                                                    }
                                                    dialog.dismiss();
                                                }, throwable -> {
                                                    AuthProvider.INSTANCE.authSelf(false, true); //null做了处理的
                                                    new RxErrorHandler().call(throwable);
                                                    dialog.dismiss();
                                                });

                                    }
                                })
                                .setNegativeButton("我再浪会...", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        dropInBuilder.create().show();
                    }

                    return true;
                }
            });

            /**
             * 绑定模块
             */

            Preference libBindPref = findPreference(getString(R.string.pref_bind_settings));
            libBindPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                            .setMessage("是否要跳转到绑定页面")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext, BindActivity.class);
                                    mContext.startActivity(intent);
                                }
                            });
                    builder.create().show();
                    return true;
                }
            });


            Preference isBindTju = findPreference(getString(R.string.pref_is_bind_tju));
            isBindTju.setSummary(CommonPreferences.INSTANCE.isBindTju() ? "已绑定" : "未绑定");
            isBindTju.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                            .setTitle("办公网解绑")
                            .setMessage("是否要解绑办公网")
//                            .setCancelable(false)
                            .setPositiveButton("解绑", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (CommonPreferences.INSTANCE.isBindTju()) {
                                        RealAuthApi.INSTANCE
                                                .unbindTju(CommonPreferences.INSTANCE.getTwtuname())
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .doAfterTerminate(() -> AuthProvider.INSTANCE.authSelf(false, true))
                                                .subscribe(responseBody -> {
                                                    Toasty.success(mContext, "解绑成功！请重启微北洋", Toast.LENGTH_SHORT).show();
                                                }, new RxErrorHandler());
                                    } else {
                                        Toasty.warning(mContext, "你没绑定解绑啥？？？？？\n点击上面按钮进入绑定页面", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("再绑会...", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                    return false;
                }
            });

            Preference isBindLib = findPreference(getString(R.string.pref_is_bind_lib));
            isBindLib.setSummary(CommonPreferences.INSTANCE.isBindLibrary() ? "已绑定" : "未绑定");
            isBindLib.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                            .setTitle("图书馆解绑")
                            .setMessage("是否要解绑图书馆")
//                            .setCancelable(false)
                            .setPositiveButton("解绑", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (CommonPreferences.INSTANCE.isBindLibrary()) {
                                        new TjuLibProvider(mContext).unbindLibrary(s -> {
                                            CommonPreferences.INSTANCE.setBindLibrary(false);
                                            Toasty.success(mContext, "解绑成功！请重启微北洋", Toast.LENGTH_SHORT).show();
                                            isBindLib.setSummary(CommonPreferences.INSTANCE.isBindLibrary() ? "已绑定" : "未绑定");
                                            dialog.dismiss();
                                        });
                                    } else {
                                        Toast.makeText(mContext, "你没绑定解绑啥？？？？？\n点击上面按钮进入绑定页面", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .setNegativeButton("再绑会...", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                    return false;
                }
            });

            Preference isBindBike = findPreference(getString(R.string.pref_is_bind_bike));
            isBindBike.setSummary(CommonPreferences.INSTANCE.isBindBike() ? "已绑定" : "未绑定");
            isBindBike.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                            .setTitle("自行车解绑")
                            .setMessage("是否要解绑自行车")
//                            .setCancelable(false)
                            .setPositiveButton("解绑", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (CommonPreferences.INSTANCE.isBindBike()) {
                                        new BikeServiceProvider(mContext).unbind();
                                    } else {
                                        Toasty.warning(mContext, "你没绑定解绑啥？？？？？\n进入自行车模块完成绑定", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("再绑会...", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                    return false;
                }
            });

            Preference isDisplayBike = findPreference(getString(R.string.pref_is_display_bike));
            isDisplayBike.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.equals(true)) {
                        Toast.makeText(mContext, "打开自行车模块以完成自行车功能的激活", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });

//            Preference isChangeSourceEnabled = findPreference(getString(R.string.pref_is_switch_news_source));
//            if (BuildConfig.DEBUG){
//                isChangeSourceEnabled.setEnabled(true);
//            }else {
//                isChangeSourceEnabled.setEnabled(false);
//            }

            Preference devTalking = findPreference(getString(R.string.pref_dev_talking));
            devTalking.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(mContext, DevTalkActivity.class);
                    mContext.startActivity(intent);
                    return false;
                }
            });

            Preference feedback = findPreference(getString(R.string.pref_feedback));
            feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://support.twtstudio.com/category/6/%E7%A7%BB%E5%8A%A8%E5%AE%A2%E6%88%B7%E7%AB%AF"));
//                    intent.createChooser(intent,"选择浏览器");
                    mContext.startActivity(intent);
                    return false;
                }
            });

            Preference contact = findPreference(getString(R.string.perf_contact_me));
            contact.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    try {
                        String email = "mobile@twtstudio.com";
                        Uri uri = Uri.parse("mailto:" + email);
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                        mContext.startActivity(emailIntent);
                    } catch (Exception e) {
                        Toasty.error(mContext, "无法启动邮件发送APP", Toast.LENGTH_SHORT).show();
                        CrashReport.postCatchedException(e);
                        e.printStackTrace();
                    }
                    return false;
                }
            });

            Preference checkUpdate = findPreference(getString(R.string.pref_check_update));
            checkUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    UpdateManager.getInstance().checkUpdate(getActivity());
                    return true;
                }
            });

            SwitchPreference isAutoCheckUpdate = (SwitchPreference) findPreference(getString(R.string.pref_is_auto_check_update));
            isAutoCheckUpdate.setChecked(UpdateManager.getInstance().getIsAutoCheck());
            isAutoCheckUpdate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Boolean value = (Boolean) newValue;
                    UpdateManager.getInstance().setIsAutoCheck(value);
                    return true;
                }
            });

            Preference proxySetting = findPreference(getString(R.string.pref_proxy_settings));
            proxySetting.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    View dialogView = inflater.inflate(R.layout.dialog_proxy_setting,null,false);
                    final EditText proxyIP = dialogView.findViewById(R.id.edit_proxy_ip);
                    proxyIP.setText(CommonPreferences.INSTANCE.getProxyAddress());
//                    prxoyIP.setText();
                    final EditText proxyPort = dialogView.findViewById(R.id.edit_proxy_port);
                    proxyPort.setText(String.valueOf(CommonPreferences.INSTANCE.getProxyPort()));

                    AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("Proxy Settings")
                            .setView(dialogView)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String ip = proxyIP.getText().toString();
                                    String port = proxyPort.getText().toString();
                                    CommonPreferences.INSTANCE.setProxyAddress(ip);
                                    CommonPreferences.INSTANCE.setProxyPort(Integer.parseInt(port));
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                    return true;
                }
            });


        }

        private void processExitTju() {
            // TODO: 23/03/2017 退学逻辑
        }
    }


}
