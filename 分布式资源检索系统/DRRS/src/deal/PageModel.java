package deal;
import java.util.*;

public class PageModel {
	public int totalCount = 0;// �ܼ�¼��
	 public int pageCount;// ��ҳ��
	 public int pageSize = 10;// ÿҳ��ʾ��¼��
	 public int page = 1;// ��ǰҳ
	 public int num = 5;

	 public Map<Map.Entry<org.apache.lucene.document.Document, Float>, Float> items;// ��ǰҳ��¼���ݼ���
	 public int prev;// ǰһҳ
	 public int next;// ��һҳ
	 public int last;// ���һҳ
	 public List<Integer> prevPages;// �õ�ǰnumҳ�����ݼ���
	 public List<Integer> nextPages;// �õ���numҳ�����ݼ���

	 public void setTotalCount(int totalCount) {
	  if (totalCount > 0) {
	   this.totalCount = totalCount;
	   this.pageCount = (totalCount + pageSize - 1) / pageSize;
	  }
	 }

	 public boolean getIsPrev() {
	  if (page > 1) {
	   return true;
	  }
	  return false;
	 }
	 
	 public int getPrev() {
	  if (getIsPrev()) {
	   return page - 1;
	  } else {
	   return page;
	  }
	 }

	 public boolean getIsNext() {
	  if (page < pageCount) {
	   return true;
	  }
	  return false;
	 }

	 public int getNext() {
	  if (getIsNext()) {
	   return page + 1;
	  }
	  return getPageCount();
	 }

	 public int getLast() {
	  return pageCount;
	 }

	 public  List<Integer> getPrevPages() {
		 List<Integer> list = new  ArrayList<Integer>();
	  int _frontStart = 1;
	  if (page > num) {
	   _frontStart = page - num;
	  } else if (page <= num) {
	   _frontStart = 1;
	  }
	  for (int i = _frontStart; i < page; i++) {
	   list.add(i);
	  }
	  return list;
	 }

	 public List<Integer> getNextPages() {
		 List<Integer> list = new ArrayList<Integer>();
	  int _endCount = num;
	  if (num < pageCount && (page + num) < pageCount) {
	   _endCount = page + _endCount;
	  } else if ((page + num) >= pageCount) {
	   _endCount = pageCount;
	  }
	  for (int i = page + 1; i <= _endCount; i++) {
	   list.add(i);
	  }
	  return list;
	 }

	 public int getPageSize() {
	  return pageSize;
	 }

	 public void setPageSize(int pageSize) {
	  this.pageSize = pageSize;
	 }

	 public int getPage() {
	  return page;
	 }

	 public void setPage(int page) {
	  this.page = page;
	 }

	 public int getNum() {
	  return num;
	 }

	 public void setNum(int num) {
	  this.num = num;
	 }

	 public Map<Map.Entry<org.apache.lucene.document.Document, Float>, Float> getItems() {
	  return items;
	 }

	 public void setItems(Map<Map.Entry<org.apache.lucene.document.Document, Float>,Float> items) {
	  this.items = items;
	 }

	 public int getTotalCount() {
	  return totalCount;
	 }

	 public int getPageCount() {
	  return pageCount;
	 }
	}
