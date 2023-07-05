package network.vena.cooperation.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    /**
     * 泛型实现将List按照每页多少条拆分，再次组装集合包含集合
     * @param list
     * @param pageSize
     * @return
     */
    public static <T> List<List<T>>   splitList(List<T> list, int pageSize){
        List<List<T>> ListAll = new ArrayList<List<T>>();
        int start=0;
        int end=0;
        int len=list.size();
        int  size=(int) Math.ceil(len/(float)pageSize);
        for(int i=0;i<size;i++){
            start=i*pageSize;
            if(i==(size-1)){
                end=len;
            }else{
                end=(i+1)*pageSize;
            }
            List<T> subList=(List<T>) list.subList(start, end);
            ListAll.add(subList);
        }
        return ListAll;
    }


    /**
     * 将List集合按页数组装成集合中包含集合
     * @param list
     * @param pageNum
     * @return
     */
    public static  <T> List<List<T>>  ListByPageNum(List<T> list, int pageNum){
        List<List<T>> ListAll = new ArrayList<List<T>>();
        int start=0;
        int end=0;
        int len=list.size();
        int  pageSize=(int) Math.ceil(len/(float)pageNum);
        System.out.println("每页对的条数为"+pageSize);
        for(int i=0;i<pageNum;i++){
            start=i*pageSize;
            if(i==(pageNum-1)){
                end=len;
            }else{
                end=(i+1)*pageSize;
            }
            List<T> subList=(List<T>) list.subList(start, end);
            ListAll.add(subList);
        }
        return ListAll;
    }

    /**
     * 用subList()实现分页
     * @param list
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static  <T> List<T>  ListPagingUtils(List<T> list, int pageNum,int pageSize){
        int start=0,end=0;
        int len=list.size();
        int  pages=(int) Math.ceil(len/(float)pageSize);
        System.out.println("总页数"+pages);
        if(pageNum<1){pageNum=1;}
        if(pageNum>=1&&pageNum<pages){
            start=(pageNum-1)*pageSize;
            end=pageNum*pageSize;
        }
        if(pageNum>=pages){
            start=(pages-1)*pageSize;
            end=pageNum*pageSize;
            if(end>=len){
                end=len;
            }
        }
        System.out.println("截取的开始数量 "+start+"截取的结束数量 "+end);
        List<T> subList=(List<T>) list.subList(start, end);
        return subList;
    }
}
