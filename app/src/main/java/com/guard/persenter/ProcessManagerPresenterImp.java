package com.guard.persenter;

import android.os.AsyncTask;

import com.guard.App;
import com.guard.model.utils.ProcessUtil;
import com.guard.model.utils.StorageUtil;
import com.guard.ui.activities.ProcessManagerActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard.presenter
 * @description: description
 * @date: 2018/6/26
 * @time: 0:42
 */
public class ProcessManagerPresenterImp implements IBasePresenter.ProcessManagerPresenter<ProcessManagerActivity> {

    private ProcessManagerActivity mHoster;
    private ProcessManagerBean smBean;
    private ArrayList<ProcessUtil.ProcessBean> mKilledProcessList;

    public ProcessManagerPresenterImp(ProcessManagerActivity mHoster) {
        onAttach(mHoster);
    }

    @Override

    public void onDetach() {
        mHoster = null;
    }

    @Override
    public void loadData() {
        LoadTask loadTask = new LoadTask(this);
        loadTask.execute();
    }

    @Override
    public void judgeTitleWhatShow(int position) {
        if (smBean != null) {
            List<ProcessUtil.ProcessBean> useProcessList = smBean.processListBean.useProcessList;
            List<ProcessUtil.ProcessBean> sysProcessList = smBean.processListBean.sysProcessList;
            if (!useProcessList.isEmpty()) {
                if (position < useProcessList.size() + 1) {
                    mHoster.showAppTitle("用户程序" + useProcessList.size() + "个");
                }
                if (!sysProcessList.isEmpty()) {
                    if (useProcessList.size() + 1 < position) {
                        mHoster.showAppTitle("系统程序" + sysProcessList.size() + "个");
                    }
                }
            } else {
                if (!sysProcessList.isEmpty()) {
                    if (position < useProcessList.size() + 1) {
                        mHoster.showAppTitle("系统程序" + sysProcessList.size() + "个");
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(ProcessManagerActivity host) {
        mHoster = new WeakReference<>(host).get();
    }

    public void notifyItemSelected(int position) {
        if (smBean != null) {
            List<ProcessUtil.ProcessBean> useProcessList = smBean.processListBean.useProcessList;
            List<ProcessUtil.ProcessBean> sysProcessList = smBean.processListBean.sysProcessList;
            if (useProcessList == null || useProcessList.isEmpty()) {
                if (position != 0) {
                    ProcessUtil.ProcessBean processBean = sysProcessList.get(position - 1);
                    processBean.setChecked(!processBean.isChecked());
                    mHoster.notifyDataSetChanged(smBean.processListBean);
                }
            } else {
                if (0 < position && position < useProcessList.size() + 1) {
                    ProcessUtil.ProcessBean processBean = useProcessList.get(position - 1);
                    boolean checked = processBean.isChecked();
                    processBean.setChecked(!checked);
                    mHoster.notifyDataSetChanged(smBean.processListBean);

                } else if (useProcessList.size() + 1 < position && position < sysProcessList.size() + 2) {
                    ProcessUtil.ProcessBean processBean = sysProcessList.get(position - useProcessList.size() - 2);
                    boolean checked = processBean.isChecked();
                    processBean.setChecked(!checked);
                    mHoster.notifyDataSetChanged(smBean.processListBean);
                }

            }
        }
    }

    public void notifyItemAllSelected() {
        if (smBean != null) {
            List<ProcessUtil.ProcessBean> useProcessList = smBean.processListBean.useProcessList;
            List<ProcessUtil.ProcessBean> sysProcessList = smBean.processListBean.sysProcessList;
            if (useProcessList != null && !useProcessList.isEmpty()) {
                for (ProcessUtil.ProcessBean userProcess : useProcessList) {
                    userProcess.setChecked(true);
                }
            }
            if (sysProcessList != null && !sysProcessList.isEmpty()) {
                for (ProcessUtil.ProcessBean systemProcess : sysProcessList) {
                    systemProcess.setChecked(true);
                }
            }
            mHoster.notifyDataSetChanged(smBean.processListBean);
        }
    }

    public void notifyItemReverse() {
        if (smBean != null) {
            List<ProcessUtil.ProcessBean> useProcessList = smBean.processListBean.useProcessList;
            List<ProcessUtil.ProcessBean> sysProcessList = smBean.processListBean.sysProcessList;
            if (useProcessList != null && !useProcessList.isEmpty()) {
                for (ProcessUtil.ProcessBean userProcess : useProcessList) {
                    userProcess.setChecked(!userProcess.isChecked());
                }
            }
            if (sysProcessList != null && !sysProcessList.isEmpty()) {
                for (ProcessUtil.ProcessBean systemProcess : sysProcessList) {
                    systemProcess.setChecked(!systemProcess.isChecked());
                }
            }
            mHoster.notifyDataSetChanged(smBean.processListBean);
        }
    }

    public void killCheckedProcess() {
        boolean flag;
        long deleteMemory = 0;
        if (smBean != null) {
            List<ProcessUtil.ProcessBean> useProcessList = smBean.processListBean.useProcessList;
            List<ProcessUtil.ProcessBean> sysProcessList = smBean.processListBean.sysProcessList;
            if (useProcessList != null && !useProcessList.isEmpty()) {
                flag = true;
                Iterator<ProcessUtil.ProcessBean> iterator = useProcessList.iterator();
                while (iterator.hasNext()) {
                    ProcessUtil.ProcessBean userProcess = iterator.next();
                    if (userProcess.isChecked()) {
                        boolean result = ProcessUtil.Companion.killProcess(userProcess.getPackageName());
                        result = true;
                        if (result) {
                            deleteMemory += userProcess.getMemorySize();
                            if (mKilledProcessList == null) {
                                mKilledProcessList = new ArrayList<>();
                                mKilledProcessList.add(userProcess);
                            } else {
                                mKilledProcessList.clear();
                                mKilledProcessList.add(userProcess);
                            }
                            iterator.remove();
                        }
                    }

                }
            } else {
                flag = false;
            }
            if (sysProcessList != null && !sysProcessList.isEmpty() && mHoster.getShowSystem()) {
                flag = true;
                Iterator<ProcessUtil.ProcessBean> iterator = sysProcessList.iterator();
                while (iterator.hasNext()) {
                    ProcessUtil.ProcessBean processBean = iterator.next();
                    if (processBean.isChecked()) {
                        boolean result = ProcessUtil.Companion.killProcess(processBean.getPackageName());
                        result = true;
                        if (result) {
                            deleteMemory += processBean.getMemorySize();
                            if (flag) {
                                mKilledProcessList.add(processBean);
                            } else {
                                if (mKilledProcessList == null) {
                                    mKilledProcessList = new ArrayList<>();
                                } else {
                                    mKilledProcessList.clear();
                                }
                            }
                            iterator.remove();
                        }
                    }
                }
            } else {
                if (flag) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
            if (flag) {
                mHoster.notifyDataSetChanged(smBean.processListBean);
                smBean.memoryInfo.setMemoryAvail(smBean.memoryInfo.getMemoryAvail() + deleteMemory);
                smBean.memoryInfo.notifyRate();
                mHoster.showMemoryMsg(smBean.memoryInfo);
                mHoster.showProcessMsg(smBean.processBean);
                mHoster.showToast(mKilledProcessList.size(), deleteMemory);
            }
        }
    }

    static class LoadTask extends AsyncTask<Void, Void, ProcessManagerBean> {
        private ProcessManagerPresenterImp presenter;

        public LoadTask(ProcessManagerPresenterImp presenter) {
            this.presenter = presenter;
        }

        @Override
        protected ProcessManagerBean doInBackground(Void... voids) {
            List<ProcessUtil.ProcessBean> runningProcess = ProcessUtil.Companion.getRunningProcess(App.Companion.getContext());
            ArrayList useProcessList = new ArrayList();
            ArrayList sysProcessList = new ArrayList();
            for (ProcessUtil.ProcessBean p : runningProcess) {
                if (p.isSystem()) {
                    sysProcessList.add(p);
                } else {
                    useProcessList.add(p);
                }
            }
            int totalSize = ProcessUtil.Companion.getALLProcess(App.Companion.getContext()).size();
            ProcessBean processBean = new ProcessBean(totalSize, runningProcess.size());
            StorageUtil.MemoryInfo memoryInfo = StorageUtil.Companion.getMemoryInfo(App.Companion.getContext());
            ProcessListBean listBean = new ProcessListBean(useProcessList, sysProcessList, runningProcess.size());
            ProcessManagerBean processManagerBean = new ProcessManagerBean(memoryInfo, processBean, listBean);
            presenter.smBean = processManagerBean;
            return processManagerBean;
        }

        @Override
        protected void onPostExecute(ProcessManagerBean result) {
            if (presenter.mHoster != null) {
                presenter.mHoster.notifyDataSetChanged(result.processListBean);
                presenter.mHoster.showMemoryMsg(result.memoryInfo);
                presenter.mHoster.showProcessMsg(result.processBean);
            }
        }
    }


    static class ProcessManagerBean {
        public ProcessManagerBean(StorageUtil.MemoryInfo memoryInfo, ProcessBean processBean, ProcessListBean processListBean) {
            this.memoryInfo = memoryInfo;
            this.processBean = processBean;
            this.processListBean = processListBean;
        }

        StorageUtil.MemoryInfo memoryInfo;
        ProcessBean processBean;
        ProcessListBean processListBean;
    }

    public static class ProcessListBean {
        public ProcessListBean(List<ProcessUtil.ProcessBean> sysProcessList, List<ProcessUtil.ProcessBean> useProcessList, int toatalSize) {
            this.sysProcessList = sysProcessList;
            this.useProcessList = useProcessList;
            this.toatalSize = toatalSize;
        }

        public List<ProcessUtil.ProcessBean> sysProcessList;
        public List<ProcessUtil.ProcessBean> useProcessList;
        public int toatalSize;
    }


    public static class ProcessBean {
        public ProcessBean(int totalSize, int runningSize) {
            this.totalSize = totalSize;
            this.runningSize = runningSize;
            runningRate = Math.round(runningSize * 100.0f / totalSize);

        }

        public int totalSize;
        public int runningSize;
        public int runningRate;
    }


}
