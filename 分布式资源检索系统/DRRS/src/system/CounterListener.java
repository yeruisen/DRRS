package system;

//import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import distributed.CommonTable;
import distributed.MainIPTable;

import search.Index;

public class CounterListener implements ServletContextListener {

	private java.util.Timer timer = null;

	// context注销
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		String host;
		String remote;
		try {
			//Index in = new Index();
			host = Index.getHost();
			remote = Index.getRemote();
			// 如果是一般服务器提出注销
			if (!host.equals(remote)) {
				// in.indexDestory();
				event.getServletContext().log("开始注销");
				CommonTable co = new CommonTable();
				// 向远程服务器删除自己本机的
				co.destroy(host, remote);
				event.getServletContext().log("注销成功");
			}
			// 主服务器提出注销
			else {
				MainIPTable ma = new MainIPTable();
				ma.destroy();
			}
		} catch (Exception e) {
			System.out.println("抛出异常???????");
		}
	}

	// context初始化
	@Override
	public void contextInitialized(ServletContextEvent event) {

		timer = new java.util.Timer(true);
		event.getServletContext().log("定时器已启动。");
		timer.schedule(new MyTask(event.getServletContext()), 0);
		event.getServletContext().log("已经添加任务调度表。");
	}

}
