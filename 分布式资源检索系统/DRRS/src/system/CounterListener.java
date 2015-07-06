package system;

//import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import distributed.CommonTable;
import distributed.MainIPTable;

import search.Index;

public class CounterListener implements ServletContextListener {

	private java.util.Timer timer = null;

	// contextע��
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		String host;
		String remote;
		try {
			//Index in = new Index();
			host = Index.getHost();
			remote = Index.getRemote();
			// �����һ����������ע��
			if (!host.equals(remote)) {
				// in.indexDestory();
				event.getServletContext().log("��ʼע��");
				CommonTable co = new CommonTable();
				// ��Զ�̷�����ɾ���Լ�������
				co.destroy(host, remote);
				event.getServletContext().log("ע���ɹ�");
			}
			// �����������ע��
			else {
				MainIPTable ma = new MainIPTable();
				ma.destroy();
			}
		} catch (Exception e) {
			System.out.println("�׳��쳣???????");
		}
	}

	// context��ʼ��
	@Override
	public void contextInitialized(ServletContextEvent event) {

		timer = new java.util.Timer(true);
		event.getServletContext().log("��ʱ����������");
		timer.schedule(new MyTask(event.getServletContext()), 0);
		event.getServletContext().log("�Ѿ����������ȱ�");
	}

}
