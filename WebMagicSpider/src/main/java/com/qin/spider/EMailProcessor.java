package com.qin.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.management.JMException;

/**
 * @author Damon
 * @create 2018-01-08 12:46
 **/

public class EMailProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

    public void process(Page page) {
        page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
        page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name")==null){
            //skip this page
            page.setSkip(true);
        }
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));
    }


    public Site getSite() {
        return site;
    }

    public static void main(String[] args) throws JMException {

        Spider emailSpider = Spider.create(new EMailProcessor())
                .addUrl("https://tieba.baidu.com/p/5314758062?pn=2")
                .addPipeline(new ConsolePipeline()).addPipeline(new FilePipeline("E:/webmagic/")).thread(20);
        SpiderMonitor.instance().register(emailSpider);
        SpiderMonitor.instance().
        emailSpider.start();
    }
}
