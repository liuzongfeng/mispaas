package rest.page.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class PageUtil {
	/**
	 * 页面初始化
	 * @param datanum 所有数据条数
	 * @param page 当前页面
	 * 如查询符合条件的条数为10条 当前查看页面第1页
	 * initpage(10,'1');即可
	 */
	public Pageinfo initpage(Integer datanum,String page)
	{
		Pageinfo pageif=new Pageinfo();
		int pageshownum=10;//每页展示数
		pageif.setShownum(pageshownum);
		pageif.setFirstpage(1);
		Integer allpage=(datanum>0)?(((datanum)%pageshownum==0)?(datanum/pageshownum):(datanum/pageshownum)+1):1;
		pageif.setAllpage(allpage);
		pageif.setLastpage(allpage);
		Integer pagenum=0;
		
		if(page==null)
		{
			pageif.setPagenum(1);
		}else
		{
			pagenum=Integer.parseInt(page);
			pageif.setPagenum(pagenum);
		}
		pageif.setPrev((pageif.getPagenum()-1)>0?pageif.getPagenum()-1:1);
		pageif.setNext((pageif.getPagenum()+1)<allpage?pageif.getPagenum()+1:allpage);
		String pageStr="";
		if(pagenum-3<=1||allpage<=7)
		{
			for (int i = 1; i <= ((allpage>7)?7:allpage); i++) {
				if(i==((allpage>7)?7:allpage))
				{
					pageStr+=i;
				}else
				{
					pageStr+=i+",";
				}
			}
			if(allpage>7)
			{
				pageStr+=",... ";
			}
		}else
		{
			pageStr+=" ...,";
			int num=((((pagenum+3>allpage)?allpage:pagenum)>3)?3:((pagenum+3>allpage)?allpage:pagenum));
			int cnum=(num+pagenum)>allpage?(allpage-pagenum):num;
			for (int i = cnum-6; i <=cnum; i++) {
				if(i==cnum)
				{
					if(cnum+pagenum==allpage)
					{
						pageStr+=(pagenum+i);
					}else
					{
						pageStr+=(pagenum+i)+",... ";
					}
				}else
				{
					pageStr+=(pagenum+i)+",";
				}
			}
		}
		pageif.setPageStr(pageStr);
		pageif.setBegin((pagenum-1)*pageshownum);
		return pageif;
	}
	
//	@Bean
//	public PageUtil getPageUtil()
//	{
//		return new PageUtil();
//	}
}
