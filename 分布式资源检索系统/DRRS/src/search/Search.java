package search;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
//import org.apache.lucene.document.MapFieldSelector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
//import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
//import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
//import org.apache.lucene.search.vectorhighlight.BaseFragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.search.vectorhighlight.FragListBuilder;
import org.apache.lucene.search.vectorhighlight.FragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.ScoreOrderFragmentsBuilder;
import org.apache.lucene.search.vectorhighlight.SimpleFragListBuilder;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class Search {
	
	/*
	 * ��Index�е���·�����ú���ΪSearch�е�·����ֵ
	 */
	
	private static String INDEX_DIR= "";//��ͨ����������ַ
	private static String INDEX_PREDIR= "";//��ȷ����������ַ
	private static String INDEX_FULLDIR = "";//ȫ�ļ���������ַ
	private static String INDEX_SYNDIR = "";//ͬ���������ַ
	
	private IndexSearcher search;	//������
	private Analyzer analyzer;		//�ִ���
	
	private QueryParser parser;		//��ѯ����������
	private Query query;			//��ѯ����
	
	private String searchStr = "";
	
	
	//���캯��
	public Search() throws Exception{
		
		analyzer=new IKAnalyzer();
		
	}
	
	//������ͨ��������·��
	public static void SetINDEX_DIR(String dir){
		
		INDEX_DIR = dir;
		
	}
	
	//���þ�ȷ��������·��
	public static void SetINDEX_PREDIR(String dir){
		
		INDEX_PREDIR = dir;
		
	}
	
	//����ȫ�ļ�������·��
	public static void SetINDEX_FULLDIR(String dir){
		
		INDEX_FULLDIR = dir;
		
	}
	
	//���������������·��(ͬ�������·��)
	public static void SetINDEX_SYNDIR(String dir){
		
		INDEX_SYNDIR = dir;
		
	}
	
	//search,ȫ�ļ���
	public Map<List<String>,Float> fullTextSearch(String sstr, String type[]) throws Exception{
		
		System.out.println(INDEX_FULLDIR);
		
		//Ԥ��������
		String terms[] = getTermStr(sstr);	//�û������ַ�����""�ڵ�����
		searchStr = expPrec(searchStr);
//		searchStr = expPrec(sstr);
//		String terms[] = getTermStr(searchStr);	//�û������ַ�����""�ڵ�����
		type = expType(type);
		
		//���ؽ������ѯ���
		Map<List<String>,Float> map = new LinkedHashMap<List<String>, Float>();
		TopDocs hits;
		
		//��ѯ
		query = getFullQuery(searchStr, type, terms);
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(INDEX_FULLDIR)));
		search = new IndexSearcher(reader);
		
		hits = search.search(query,1000);
		
		//i ƥ��Ľ����
		int i = hits.totalHits;
		i = i<1000?i:1000;
		
		//����
		FastVectorHighlighter highlighter = getHighlighter();
		FieldQuery fieldquery = highlighter.getFieldQuery(query);	
		
		QueryScorer scorer = new QueryScorer(query);
		SimpleHTMLFormatter htmlformatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
		Highlighter h = new Highlighter(htmlformatter, scorer);
		h.setTextFragmenter(new SimpleSpanFragmenter(scorer));
		
		//�������װ��map
		for(ScoreDoc scoreDoc:hits.scoreDocs){

			String title = highlighter.getBestFragment(fieldquery, reader, scoreDoc.doc, "title", 100);
			String describe = highlighter.getBestFragment(fieldquery, reader, scoreDoc.doc, "describe", 100);
			
			String url = search.doc(scoreDoc.doc).get("url");
			
			String title1 = search.doc(scoreDoc.doc).get("title");
			
			if(title!=null &&(title.length()-25 < title1.length())){
				TokenStream t1 = analyzer.tokenStream("title", new StringReader(title1));
				title = h.getBestFragment(t1, title1);
			}
				
			
			if(title == null)
				title = title1;
			
			if(describe == null)
				describe = search.doc(scoreDoc.doc).get("describe");
			
			List<String> list = new ArrayList<String>(3);
			list.add(title);
			list.add(describe);
			list.add(url);
			
			map.put(list, scoreDoc.score);
			
		}
				
		//search.close();
		
		return map;		
		
	}
	
	
	//search����ͨ��������
	public Map<List<String>,Float> search(String sstr,String type[])throws Exception{
	
		System.out.println(INDEX_DIR);
		
		//Ԥ��������
		String []terms = getTermStr(sstr);
		searchStr = expPrec(searchStr);
//		searchStr = expPrec(sstr);
//		String []terms = getTermStr(searchStr);
		type = expType(type);

		
		//���ؽ������ѯ���
		Map<List<String>,Float> map = new LinkedHashMap<List<String>, Float>();
		TopDocs hits;

		
		//��ѯ

		query = getQuery(searchStr, type, terms);
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(INDEX_DIR)));
		search = new IndexSearcher(reader);
		
		hits = search.search(query,1000);
		
		//i ƥ��Ľ����
		int i = hits.totalHits;
		i = i<1000?i:1000;
		
		//����
		FastVectorHighlighter highlighter = getHighlighter();
		FieldQuery fieldquery = highlighter.getFieldQuery(query);
		
		QueryScorer scorer = new QueryScorer(query);
		SimpleHTMLFormatter htmlformatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
		Highlighter h = new Highlighter(htmlformatter, scorer);
		h.setTextFragmenter(new SimpleSpanFragmenter(scorer));
		
		//�������װ��map
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			
			//System.out.println(search.doc(scoreDoc.doc).get("title"));
			
			String title = highlighter.getBestFragment(fieldquery, reader, scoreDoc.doc, "title", 100);
			String describe = highlighter.getBestFragment(fieldquery, reader, scoreDoc.doc, "describe", 100);
			
			String url = search.doc(scoreDoc.doc).get("url");
			
			String title1 = search.doc(scoreDoc.doc).get("title");
			
			if(title!=null &&(title.length()-25 < title1.length())){
				TokenStream t1 = analyzer.tokenStream("title", new StringReader(title1));
				title = h.getBestFragment(t1, title1);
			}
				
			
			if(title == null)
				title = title1;
			
			System.out.println("title:"+title);//
			
			if(describe == null)
				describe = search.doc(scoreDoc.doc).get("describe");
			
			List<String> list = new ArrayList<String>(3);
			list.add(title);
			list.add(describe);
			list.add(url);
			
			map.put(list, scoreDoc.score);
			
		}
		
		//search.close();
		
		return map;
		
	}
	
	
	//preciseSearch,��ȷ��������
	public Map<List<String>,Float> preciseSearch(String searchStrs[],String type[])throws Exception{
		
		//Ԥ��������
		int i = searchStrs.length;
		String terms[][] = new String[2][];
		
		for(int j = 0; j < i; j++){
			if(searchStrs[j]==null || searchStrs[j].equals("")){			
				searchStrs[j]="";
			}
			else if(j==0||j==1){
				
				terms[j] = getTermStr(searchStrs[j]);
				searchStrs[j] = expPrec(searchStr);
				
			}
		}
		
		type=expType(type);
		
		//��������
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(INDEX_PREDIR)));
		search = new IndexSearcher(reader);
		Map<List<String>,Float> map = new LinkedHashMap<List<String>, Float>();
		
		//��ѯ
		query = getPreciseQuery(searchStrs, type, terms);
		TopDocs hits = search.search(query, 100);
		
		//i ƥ��Ľ����
		i = hits.totalHits;
		i = i<100?i:100;
		
		//����
		QueryScorer scorer = new QueryScorer(query);
		SimpleHTMLFormatter htmlformatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
		Highlighter highlighter = new Highlighter(htmlformatter, scorer);
		highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
		
		//�������װ��map
		for(int j = 0; j < i; j++){
			
			Document doc = search.doc(hits.scoreDocs[j].doc);
			
			String title = doc.get("title");
			
			if(title != null){
				
				TokenStream t1 = analyzer.tokenStream("title", new StringReader(title));
				String s = highlighter.getBestFragment(t1, title);
				
				if(s != null)
					title = s;
			}
			
			String describe = doc.get("describe");
			
			if(describe != null){
				
				TokenStream t2 = analyzer.tokenStream("describe", new StringReader(describe));
				String s = highlighter.getBestFragment(t2,describe);
				
				if(s != null)
					describe = s;
			}
			
			String url = doc.get("url");
			
			List<String> list = new ArrayList<String>(3);
			list.add(title);
			list.add(describe);
			list.add(url);
			
			map.put(list, hits.scoreDocs[j].score);
			
		}
		
		//search.close();
		
		return map;
		
	}
	
	//��ѯͬ���,�����������
	private String[] getSyns(String str){
		
		String syns[] = {"*"};
		
		QueryParser parser = new QueryParser(Version.LUCENE_36, "word", analyzer);
		try{
			
			Query q = parser.parse(str);
			IndexReader reader = IndexReader.open(FSDirectory.open(new File(INDEX_SYNDIR)));
			search = new IndexSearcher(reader);
			TopDocs hits = search.search(q, 3);
			
			//ƥ��Ľ����
			int i = hits.totalHits;
			if(i > 0){
				syns = new String[i];
				
				for(int j = 0; j < i; j++){
					
					syns[j] = search.doc(hits.scoreDocs[j].doc).get("syn");
					
				}
			}
			
		}
		catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return syns;
		
	}
	
	//���query��������ͨ����
	private Query getQuery(String searchStr,String type[], String terms[]) {
		
		for(String st : terms)//
			System.out.println(st);//
		
		System.out.println("searchStr:"+searchStr);
		
		BooleanQuery query = new BooleanQuery();
		Query q;
		
		if(searchStr!=null && searchStr.length() != 0){
			
			BooleanQuery queryS = new BooleanQuery();
			
			try{
				
				parser = new QueryParser(Version.LUCENE_36, "title", analyzer);
				q = parser.parse(searchStr);
				queryS.add(q, Occur.SHOULD);
				
				parser = new QueryParser(Version.LUCENE_36, "keywords", analyzer);
				q = parser.parse(searchStr);
				queryS.add(q, Occur.SHOULD);
				
				parser = new QueryParser(Version.LUCENE_36, "describe", analyzer);
				q = parser.parse(searchStr);
				queryS.add(q, Occur.SHOULD);
				
				parser = new QueryParser(Version.LUCENE_36, "author", analyzer);
				q = parser.parse(searchStr);
				queryS.add(q, Occur.SHOULD);
				
				parser = new QueryParser(Version.LUCENE_36, "publisher", analyzer);
				q = parser.parse(searchStr);
				queryS.add(q, Occur.SHOULD);
				
				parser = new QueryParser(Version.LUCENE_36, "kind", analyzer);
				q = parser.parse(searchStr);
				queryS.add(q, Occur.SHOULD);
				
				for(String tm : terms)
					if(tm != null && !("".equals(tm))){
						
						q = new TermQuery(new Term("title", tm));
						queryS.add(q, Occur.SHOULD);
						
						q = new TermQuery(new Term("keywords", tm));
						queryS.add(q, Occur.SHOULD);
						
						q = new TermQuery(new Term("describe", tm));
						queryS.add(q, Occur.SHOULD);
						
						q = new TermQuery(new Term("author", tm));
						queryS.add(q, Occur.SHOULD);
						
						q = new TermQuery(new Term("publisher", tm));
						queryS.add(q, Occur.SHOULD);
						
						q = new TermQuery(new Term("kind", tm));
						queryS.add(q, Occur.SHOULD);
						
					}
				
			}
			catch(Exception e){
				
				e.printStackTrace();
				System.out.print("Search.getQuery");
				
			}
		
			queryS.setMinimumNumberShouldMatch(1);
			query.add(queryS, Occur.MUST);
			
		}
		
		if(type[0] != null && type[0] != ""){
			
			BooleanQuery queryT = new BooleanQuery();
			
			int j = type.length;
			
			for(int i = 0; i < j && type[i] != null; i++){
				
				Term t = new Term("kind", type[i]);
				
				q = new TermQuery(t);
				
				queryT.add(q, Occur.SHOULD);
				
			}
			
			queryT.setMinimumNumberShouldMatch(1);
			query.add(queryT, Occur.MUST);
			
		}
		
		//�����������
		String syns[] = getSyns(searchStr);
		if(!("*".equals(syns[0]))){		
			BooleanQuery querysyn = new BooleanQuery();
			for(int j = 0; j < syns.length; j++){
				System.out.println("syn:"+syns[j]);
				querysyn.add(new TermQuery(new Term("title",syns[j])), Occur.SHOULD);
				querysyn.add(new TermQuery(new Term("keywords",syns[j])), Occur.SHOULD);
				querysyn.add(new TermQuery(new Term("describe",syns[j])), Occur.SHOULD);
			}
			
			query.add(querysyn, Occur.SHOULD);
		}
		
		return query;
		
	} 
	
	
	//��ȡQuery�����ھ�ȷ����
	private Query getPreciseQuery(String str[], String type[], String[][] terms) throws Exception{
		
		BooleanQuery query = new BooleanQuery();
		Term t;
		Query q;
		
		//title����ɲ��title���ڹؼ��ֲ�ѯ
		if((str[0] != null && str[0].length() != 0)||(!(terms[0] == null))){
			
			//System.out.println("title*******");
			
			BooleanQuery bq = new BooleanQuery();
			
			if(str[0] != null && str[0].length() != 0){
				System.out.println("str[0]:"+str[0]);//
				parser = new QueryParser(Version.LUCENE_36, "title", analyzer);
				q = parser.parse(str[0]);
				bq.add(q, Occur.SHOULD);		
			}
			
			if(!(terms[0] == null)){
				int count = terms[0].length;
				if(count>0){
					for(String term : terms[0])
						if(term != null && !term.equals("")){
							System.out.println("term:"+term);//
							Query tq = new TermQuery(new Term("title", term));
							bq.add(tq, Occur.SHOULD);
						}
				}	
			}
			
			bq.setMinimumNumberShouldMatch(1);
	
			query.add(bq,Occur.MUST);
		}
		
		//keywords������ڹؼ��ֲ�ѯ
		if((str[1] != null && str[1].length()!=0)||(!(terms[1] == null))){
			
			//System.out.println("keyword**************");
			
			BooleanQuery bq = new BooleanQuery();
			
			if(str[1] != null && str[1].length()!=0){
				System.out.println("str[1]:"+str[1]);//
				parser = new QueryParser(Version.LUCENE_36, "keywords", analyzer);
				q = parser.parse(str[1]);
				bq.add(q, Occur.SHOULD);
			}
			
			if(!(terms[1] == null)){
				if(terms[1].length>0){
					for(String term : terms[1])
						if(term!=null && !"".equals(term)){
							System.out.println("term:"+term);
							TermQuery tq = new TermQuery(new Term("keywords", term));
							bq.add(tq, Occur.SHOULD);				
						}
				}		
			}
			
			bq.setMinimumNumberShouldMatch(1);
			
			query.add(bq, Occur.MUST);
		}
		
		//author�����ز��������ȫƥ��
		if(str[2] != null && str[2].length()!=0){
			
			t = new Term("author", str[2]);
			q = new TermQuery(t);
			
			query.add(q, Occur.MUST);
			
		}
		
		//publisher����ɲ��������ȫƥ��
		if(str[3] != null && str[3].length() != 0){
			
			t = new Term("publisher", str[3]);
			q = new TermQuery(t);
			
			query.add(q, Occur.MUST);
			
		}
		//String s=str[4]+"-"+str[5]+"-"+str[6];
		//t=new Term("data",s);
		
		//type����
		if(type[0] != null && type[0] != ""){
			
			BooleanQuery queryT = new BooleanQuery();
			
			int j = type.length;
			for(int i = 0; i < j && type[i] != null; i++){  
				
				t = new Term("kind", type[i]);
				q = new TermQuery(t);
				
				queryT.add(q, Occur.SHOULD);
				
			}
			
			queryT.setMinimumNumberShouldMatch(1);
			query.add(queryT, Occur.MUST);
			
		}
		
		return query;
	}
	
	//���query ����ȫ�ļ���
	private Query getFullQuery(String searchStr,String type[], String terms[]) throws Exception{
		
		BooleanQuery query = new BooleanQuery();
		Query q;
		
		if(searchStr!=null && searchStr.length() != 0){
			
			BooleanQuery queryS = new BooleanQuery();
			
			try{
				
				parser = new QueryParser(Version.LUCENE_36, "title", analyzer);
				q = parser.parse(searchStr);
				queryS.add(q, Occur.SHOULD);
				
//				parser = new QueryParser(Version.LUCENE_36, "keywords", analyzer);
//				q = parser.parse(searchStr);
//				queryS.add(q, Occur.SHOULD);
				
				parser = new QueryParser(Version.LUCENE_36, "describe", analyzer);  //ȥ����Ӱ�������
				q = parser.parse(searchStr);
				queryS.add(q, Occur.SHOULD);
				
				parser = new QueryParser(Version.LUCENE_36, "author", analyzer);
				q = parser.parse(searchStr);
				queryS.add(q,Occur.SHOULD);
				
				parser = new QueryParser(Version.LUCENE_36, "publisher", analyzer);
				q = parser.parse(searchStr);
				queryS.add(q, Occur.SHOULD);
				
				parser = new QueryParser(Version.LUCENE_36, "kind", analyzer);
				q = parser.parse(searchStr);
				queryS.add(q, Occur.SHOULD);
				
				parser = new QueryParser(Version.LUCENE_36, "content", analyzer);
				q = parser.parse(searchStr);
				queryS.add(q, Occur.SHOULD);
				
				for(String tm : terms)
					if(tm != null && !("".equals(tm))){
						
						q = new TermQuery(new Term("title", tm));
						queryS.add(q, Occur.SHOULD);
						
//						q = new TermQuery(new Term("keywords", tm));
//						queryS.add(q, Occur.SHOULD);
						
						q = new TermQuery(new Term("describe", tm));
						queryS.add(q, Occur.SHOULD);
						
						q = new TermQuery(new Term("author", tm));
						queryS.add(q, Occur.SHOULD);
						
						q = new TermQuery(new Term("publisher", tm));
						queryS.add(q, Occur.SHOULD);
						
						q = new TermQuery(new Term("kind", tm));
						queryS.add(q, Occur.SHOULD);
						
						q = new TermQuery(new Term("content", tm));
						queryS.add(q, Occur.SHOULD);
						
					}
				
			}
			catch(Exception e){
				
				e.printStackTrace();
				System.out.print("Search.getQuery");
				
			}
		
			queryS.setMinimumNumberShouldMatch(1);
			query.add(queryS, Occur.MUST);
			
		}
		
		if(type[0] != null && type[0] != ""){
			
			BooleanQuery queryT = new BooleanQuery();
			
			int j = type.length;
			
			for(int i = 0; i < j && type[i] != null; i++){
				
				Term t = new Term("kind", type[i]);
				
				q = new TermQuery(t);
				
				queryT.add(q, Occur.SHOULD);
				
			}
			
			queryT.setMinimumNumberShouldMatch(1);
			query.add(queryT, Occur.MUST);
			
		}
		
		//�����������
		String syns[] = getSyns(searchStr);
		if(!("*".equals(syns[0]))){		
			BooleanQuery querysyn = new BooleanQuery();
			for(int j = 0; j < syns.length; j++){
				querysyn.add(new TermQuery(new Term("title",syns[j])), Occur.SHOULD);
				querysyn.add(new TermQuery(new Term("keywords",syns[j])), Occur.SHOULD);
				querysyn.add(new TermQuery(new Term("describe",syns[j])), Occur.SHOULD);
			}
			
			query.add(querysyn, Occur.SHOULD);
		}
		
		return query;
		
	}
	
	
	//��ȡhighlighter
	static FastVectorHighlighter getHighlighter(){
		
		FragListBuilder flb = new SimpleFragListBuilder();
		FragmentsBuilder fmb = new ScoreOrderFragmentsBuilder(new String[]{"<font color='red'>"},new String[]{"</font>"});
		//FragmentsBuilder fmb = new ScoreOrderFragmentsBuilder(BaseFragmentsBuilder.COLORED_PRE_TAGS,BaseFragmentsBuilder.COLORED_POST_TAGS);
		return new FastVectorHighlighter(true, true, flb, fmb);
		
	}
	
	
	//��ȡ�ؼ���
	public List<String> getKeywords(String str){
		
		List<String> list = new ArrayList<String>();
		
		StringReader reader = new StringReader(str);
		TokenStream ts = analyzer.tokenStream("", reader);
		
		CharTermAttribute ta = ts.addAttribute(CharTermAttribute.class);
		
		try{
			
			while(ts.incrementToken()){
				
				System.out.print(ta.toString()+'|');
				
				list.add(ta.toString());
				
			}
			
		}
		catch(Exception e){
			
			e.printStackTrace();
			System.out.println("Search.getKeywords()");
			
		}
		
		return list;
		
	}
	
	
	/*******************���Ժ���***********************/
//	public Query testGQ(String str,String a)throws Exception{   //***test
//		
//		//Term t=new Term("1~2","");
//			
//		try{
//			
//			Query q;
//			parser = new QueryParser(Version.LUCENE_36,a,analyzer);
//			q=parser.parse(str);
//			
//			return q;
//			
//		}
//		catch(Exception e){
//			
//			e.printStackTrace();
//			System.out.println("WRONG");
//			return null;
//			
//		}
//		
//		//Query q=new TermQuery(t);
//		//BooleanQuery q=new BooleanQuery();
//		//return q;
//	
//	}
//	
//	
//	public void Test(String str,String t){  //***test
//		
//		//str=expPrec(str);
//		//System.out.println(str);
//		
//		try{
//			
//			search = new IndexSearcher(IndexReader.open(FSDirectory.open(new File(INDEX_DIR))));
//			Query query = testGQ(str,t);
//			
//			TopDocs hits = search.search(query, 100);
//			
//			for(ScoreDoc sd:hits.scoreDocs){
//			
//				Explanation e = search.explain(query,sd.doc);
//				System.out.println(search.doc(sd.doc).get("title")+"     "+sd.score); //***test
//				System.out.println(e.toString()+'\n');  //***test
//				
//				Document doc = search.doc(sd.doc);
//				System.out.println(doc.get(t));
//				//System.out.println(doc.get("date")+"\n");
//				
//			}
//		}
//		catch(Exception e){
//			
//			e.printStackTrace();
//			System.out.println("Search.Test()");
//			
//			}
//		
//	}
	
/*************************************************************************/	
	
	
	//***�Խ������***//
	public static Map<List<String>,Float> sort(List<Map<List<String>,Float>> maplist){
		
		Map<List<String>,Float> map = new LinkedHashMap<List<String>,Float>();
		
		int i = maplist.size();
		
		map = maplist.get(0);
		
		for(int j = 1; j < i; j++)
			map = mergesort(map,maplist.get(j));
		
		return map;
		
	}
	
	private static Map<List<String>,Float> mergesort(Map<List<String>,Float> map1,Map<List<String>,Float> map2){
		
		Map<List<String>,Float> map = new LinkedHashMap<List<String>,Float>();
		
		Iterator<Map.Entry<List<String>,Float>> i1 = map1.entrySet().iterator();
		Iterator<Map.Entry<List<String>,Float>> i2 = map2.entrySet().iterator();
		
		if(i1.hasNext()&&i2.hasNext()){
			
			Map.Entry<List<String>,Float> e1 = i1.next();
			Map.Entry<List<String>,Float> e2 = i2.next();
			
			int tab = 0;
			
			while(true){
				
				if(e1.getValue() >= e2.getValue()){
					
					map.put(e1.getKey(),e1.getValue());
					
					if(i1.hasNext())
						e1 = i1.next();
					
					else{
						
						tab = 1;
						break;
						
					}
					
				}
				else{
					
					map.put(e2.getKey(),e2.getValue());
					
					if(i2.hasNext())
						e2 = i2.next();
					else{
						tab = 2;
						break;
					}
					
				}
				
			}
			
			if(tab==1){
				
				map.put(e2.getKey(),e2.getValue());
				
				while(i2.hasNext()){
					
					e2=i2.next();
					map.put(e2.getKey(),e2.getValue());
					
				}
				
			}
			else{ 
				
				map.put(e1.getKey(),e1.getValue());
				while(i1.hasNext()){ 
					
					e1 = i1.next();
					map.put(e1.getKey(),e1.getValue());
					
				}
				
			}
			
		}
		else{
			
			if(i1.hasNext())	
				map = map1;
			
			if(i2.hasNext())
				map = map2;
			
		}
		
		return map;
		
	}
	
	
	/******�û�����Ԥ������,typeԤ������******/
	
	//����û�����""�е�����(������ͨ������ȫ�ļ���)
	private String[] getTermStr(String a){
		
		String str[] = {};	 //���ڱ���""�е����ݣ��ò������ݲ����зִ�
		
		if(a != null && a.length()!=0){
			
			a = a.trim();
			a = a.replace('��', '\"');
			a = a.replace('��', '\"');
			
			char b[] = a.toCharArray();
			int i = a.length();
				
			if(i > 2){
				
				int count = 0;
				for(int k = 0; k < i; k++){						
						
					if(b[k] == '\"')
						count ++ ;
						
				}
					
				if(count > 1){
						
					count /= 2;
					str = new String[count];
						
					for(int m = 0; m < count; m++){
							
						String s = "";

						int p = a.indexOf('\"');
						if(p == 0){
							
							a = a.substring(1);
							p = a.indexOf('\"');
							
							if(p > 0){
									
								s = a.substring(0, p);
								a = a.substring(p+1);
								
							}
							else{
								
								a = a.substring(p+1);

							}
								
						}
						else {
							
							s = a.substring(p+1);
							a = a.substring(0, p);
							
							p = s.indexOf('\"');
							if(p > 0){
								
								a = a + s.substring(p+1);
								s = s.substring(0, p);
									
							}
							else{
								
								a = a + s.substring(p+1);
								s = "";
								
							}
								
						}
						
						str[m] = s;
	
					}
				}
			}
		}
		
		searchStr = a;		//ΪsearchStr ��ֵ
		
		return str;
		
	}
	
	//���û������ַ������д���(��ͨ������ȫ�ļ���)
	private String expPrec(String a){
		
		if(a != null && a.length()!=0){
			
			a = a.trim();
			
			char b[] = a.toCharArray();
			int i = a.length(),j,t,n = 0;
			
			if(i != 0){

				char c[] = new char[i];
				boolean tab = false;
				
				for(j  =0; j < i; j++){
					
					t=(int)b[j];
					
					if(!(t >= 33 && t <= 47) && !(t >= 58 && t <= 64) && 
							!(t >= 91 && t <= 96) && !(t >= 123 && t <= 126)){	//Ѱ�ҵ�һ�����Ƿ��ŵ��ַ�
						
						c[n++] = b[j++];
						tab = true;
						break;
					}
				}
				
				for(; j < i; j++){
					
					if(!(b[j]=='{'||b[j]=='}'||b[j]=='['||b[j]==']'||b[j]=='?'
							    ||b[j]=='^'||b[j]=='('||b[j]==')'||b[j]=='*'||b[j]=='\"'))	//ȥ����Щ����
						
						c[n++] = b[j];
					
				}
				
				if(tab){
					
					a = String.valueOf(c);
					
				}
				
				else
					a = "";
				
			}
		}
		
		else a = "";
		return a;
		
	}
	
	
	//
	private String[] expType(String t[]){
		
		int i = t.length;
		
		if(i != 0){
			
			for(int j = 0; j < i && t[j] != null; j++)
				
				t[j].toLowerCase();
			
			return t;
			
		}
		else{
			
			String a[] = {null};
			t = a;
			
			return t ;
		}
	}
	
	
//	//�û������ַ���Ԥ����,(��ȷ����)
//	private String[] expPrePrec(String s[]){
//		
//		int i = s.length;
//		String term[][] = new String[2][];
//		
//		for(int j = 0; j < i; j++){
//			if(s[j]==null){			
//				s[j]="";
//			}
//			else if(j==0||j==1){
//				
//				term[j] = getTermStr(s[j]);
//				s[j] = expPrec(searchStr);
//				
//			}
//		}
//		
//		return s;
//		
//	}
}
