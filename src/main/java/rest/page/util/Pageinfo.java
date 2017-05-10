package rest.page.util;

import java.util.ArrayList;

public class Pageinfo {
	private Integer firstpage;//第一页
	private Integer prev;//上一页
	private Integer next;//下一页
	private Integer pagenum;//当前页
	private Integer allpage;//总页面
	private Integer lastpage;//最后一页
	private Integer shownum;//每页展示数
	private String pageStr; //页面字符串 以逗号分隔
	private Integer begin;//本页查询结果起始记录条码
	private Integer end;
	private Object resultObj; //查询结果对象
	private Object ConditionObj; //页面查询元素对象
	public Integer getFirstpage() {
		return firstpage;
	}
	public void setFirstpage(Integer firstpage) {
		this.firstpage = firstpage;
	}
	public Integer getPrev() {
		return prev;
	}
	public void setPrev(Integer prev) {
		this.prev = prev;
	}
	public Integer getNext() {
		return next;
	}
	public void setNext(Integer next) {
		this.next = next;
	}
	public Integer getPagenum() {
		return pagenum;
	}
	public void setPagenum(Integer pagenum) {
		this.pagenum = pagenum;
	}
	public Integer getAllpage() {
		return allpage;
	}
	public void setAllpage(Integer allpage) {
		this.allpage = allpage;
	}
	public Integer getLastpage() {
		return lastpage;
	}
	public void setLastpage(Integer lastpage) {
		this.lastpage = lastpage;
	}
	public Integer getShownum() {
		return shownum;
	}
	public void setShownum(Integer shownum) {
		this.shownum = shownum;
	}
	
	public String getPageStr() {
		return pageStr;
	}
	public void setPageStr(String pageStr) {
		this.pageStr = pageStr;
	}
	public Object getResultObj() {
		return resultObj;
	}
	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}
	public Object getConditionObj() {
		return ConditionObj;
	}
	public void setConditionObj(Object conditionObj) {
		ConditionObj = conditionObj;
	}
	
	public Integer getBegin() {
		return begin;
	}
	public void setBegin(Integer begin) {
		this.begin = begin;
	}
	public Integer getEnd() {
		return end;
	}
	public void setEnd(Integer end) {
		this.end = end;
	}
	
	public Pageinfo(Integer firstpage, Integer prev, Integer next,
			Integer pagenum, Integer allpage, Integer lastpage,
			Integer shownum, String pageStr, Integer begin, Integer end,
			Object resultObj, Object conditionObj) {
		super();
		this.firstpage = firstpage;
		this.prev = prev;
		this.next = next;
		this.pagenum = pagenum;
		this.allpage = allpage;
		this.lastpage = lastpage;
		this.shownum = shownum;
		this.pageStr = pageStr;
		this.begin = begin;
		this.end = end;
		this.resultObj = resultObj;
		ConditionObj = conditionObj;
	}
	public Pageinfo() {
		super();
	}
	
}
