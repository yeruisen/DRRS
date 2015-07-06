package deal;

import java.io.File;

public class Delete {
	public void delete1(String path){
		File file = new File(path);
		if(file.exists()){
			System.out.println("文件存在，地址:"+path);
			System.out.println("准备删除");
			boolean b = file.delete();
			if(b){
				System.out.println("删除成功！");
			}else{
				System.out.println("删除失败！");
			}
		}else{
			System.out.println("文件不存在！地址:"+path);
		}
	}
}
