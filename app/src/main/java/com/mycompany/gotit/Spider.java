package com.mycompany.gotit;

import android.util.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class Spider {

    private static final String TAG_BOOK_EXPRESS = "tag_book_express";

    private static final String TAG_BOOK_WITH_TAG = "tag_book_with_tag";

    private static final String TAG_BOOK_SEARCH = "tag_book_search";

    private static final String TAG_BOOK_LABELS = "tag_book_labels";

    private Book parseBookli(Element element){
        String mark_string = element.select("p.rating span").html();
        double mark;
        if(!mark_string.isEmpty() && !mark_string.equals("评价人数不足") && !mark_string.equals("目前无人评价"))
            mark = Double.parseDouble(element.select("p.rating").text());
        else mark = 0;
        Book book = new Book();
        book.setTitle(element.select("h2 a").text());
        book.setPublishInfo(element.select("p").get(1).text());
        book.setDetailPageUrl(element.select("a.cover").attr("href"));
        book.setIntroduction(element.select("p").get(2).text());
        book.setMark(mark);
        book.setImageUrl(element.select("img").attr("src"));
        return book;
    }

    public ArrayList<Book> getBookExpress() {
        String url = "https://book.douban.com/latest?icn=index-latestbook-all";
        ArrayList<Book> books = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        }catch (IOException ioe){
            return new ArrayList<>();
        }
        Elements elements_fiction = doc.select("div.article li");
        for(Element element : elements_fiction) {
            Book book = parseBookli(element);
            books.add(book);
            Log.d(TAG_BOOK_EXPRESS, "book express fiction book added: "+book.getTitle());
        }

        Elements elements_nonfiction = doc.select("div.aside li");
        for(Element element : elements_nonfiction) {
            Book book = parseBookli(element);
            books.add(book);
            Log.d(TAG_BOOK_EXPRESS, "book express non-fiction book added: "+book.getTitle());
        }

        return books;
    }

    private Book parseBookliSubjectItem(Element element){
        double mark;
        String mark_string = element.select("span.rating_nums").text();
        if(mark_string.length() != 0)
            mark = Double.parseDouble(mark_string);
        else mark = 0;
        Book book = new Book();
        book.setTitle(element.select("div.info h2 a").attr("title"));
        Log.d(TAG_BOOK_WITH_TAG,element.html());
        book.setPublishInfo(element.select("div.pub").text());
        book.setDetailPageUrl(element.select("div.info h2 a").attr("href"));
        if(element.select("p").isEmpty())
            book.setIntroduction("暂无简介");
        else book.setIntroduction(element.select("p").get(0).text());
        book.setMark(mark);
        book.setImageUrl(element.select("a.nbg img").attr("src"));
        return book;
    }

    @SuppressWarnings("DefaultLocale")
    public ArrayList<Book> getBookWithTag(String tag, int start){
        //type T can be expanded in the future
        Log.d(TAG_BOOK_WITH_TAG, "get book with "+tag);
        String urlPattern = "https://book.douban.com/tag/%s?start=%d&type=%s";
        ArrayList<Book> books = new ArrayList<>();
        Document doc;
        try {
            doc = Jsoup.connect(String.format(urlPattern, tag, start, "T")).timeout(2000).get();
        } catch (IOException ioe){
            Log.e(TAG_BOOK_WITH_TAG, "connect error occurs in getBookWithTag()", ioe);
            return new ArrayList<>();
        }
        Log.d(TAG_BOOK_WITH_TAG, "1");
        Elements elements = doc.select("ul.subject-list li.subject-item");
        for (Element element : elements){
            Book book = parseBookliSubjectItem(element);
            books.add(book);
            Log.d(TAG_BOOK_WITH_TAG, "get book with tag: " + book.getTitle());
        }

        Log.d(TAG_BOOK_WITH_TAG, "finished TAG "+tag);


        return books;
    }

    private Book parseBookDiv(Element element){
        Book book = new Book();
        return book;
    }

    @SuppressWarnings("defaultLocale")
    public ArrayList<Book> getSearchResult(String query, int start){
        String urlPattern = "https://search.douban.com/book/subject_search?search_text=%s&cat=1001&start=%d";
        String url = String.format(urlPattern, query, start);
        Log.d(TAG_BOOK_SEARCH, "into search");
        Document doc;
        try {
            doc = Jsoup.connect(url).timeout(2000)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36")
                    .get();
        } catch (IOException ioe){
            Log.e(TAG_BOOK_SEARCH, "connect error occurs in getBookWithTag()", ioe);
            return new ArrayList<>();
        }
        Log.d(TAG_BOOK_SEARCH, "into search");
        Elements elements = doc.select("body");
        for(Element element : elements){
            Log.d(TAG_BOOK_SEARCH, "element");
            Log.d(TAG_BOOK_SEARCH, element.html());
        }

        return new ArrayList<>();
    }

}
