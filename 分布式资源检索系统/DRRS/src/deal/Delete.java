package deal;

import java.io.File;

public class Delete {
	public void delete1(String path){
		File file = new File(path);
		if(file.exists()){
			System.out.println("�ļ����ڣ���ַ:"+path);
			System.out.println("׼��ɾ��");
			boolean b = file.delete();
			if(b){
				System.out.println("ɾ���ɹ���");
			}else{
				System.out.println("ɾ��ʧ�ܣ�");
			}
		}else{
			System.out.println("�ļ������ڣ���ַ:"+path);
		}
	}
}
