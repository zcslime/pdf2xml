package com.quandashi.qz.main;

import com.quandashi.qz.bean.PdfemleBean;
import com.quandashi.qz.utils.GetCharLocationAndSize;
import com.quandashi.qz.utils.GetImageLocationsAndSize;
import com.quandashi.qz.utils.RegexUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.*;

/**
 * @Author zc
 * @PackageName pdf2xml
 * @Package com.quandashi.qz.main
 * @Date 2023/2/3 14:58
 * @Version 1.0
 * @Describe
 */

public class Pdf2xml{
    public static void main( String[] args ) throws IOException{
        String path = "D:\\user\\pdf\\CN112836213A.pdf";
        String pn = "CN112836213A";
        File file = new File(path);
        InputStream is = null;
        PDDocument document = null;
        if(path.endsWith(".pdf")||path.endsWith(".PDF")){
            //打开PDF文件
            document = PDDocument.load(file);
            // TODO 获取图片相关信息的类
            GetImageLocationsAndSize image = new GetImageLocationsAndSize();
            // TODO 获取文字相关信息的类
            GetCharLocationAndSize fontext = new GetCharLocationAndSize();
            // TODO 获取pdf页数 一页一页读取 10页，
            int pageSize  = document.getNumberOfPages();
            StringBuffer bf = new StringBuffer();
            int numClaim = 10001;
            String patent_pn="";
            String pub_date="";
            String patent_an="";
            String app_date="";
            // TODO 说明书的列表
            ArrayList<String> desList = new ArrayList<>();
            // TODO 说明书附图的列表
            ArrayList<String> desimgList = new ArrayList<>();
            bf.append("<!--xml version=\"1.0\" encoding=\"utf-8\" -->\n");
//            //cn-bibliographic-data/application-reference
            bf.append("<cn-patent-document lang=\"ZH\" country=\"CN\" company=\"qpat\" >\n");
            for (int i = 1; i < pageSize+1; i++) {
                // TODO 文字读取
                fontext.setSortByPosition( true );
                fontext.setStartPage(i);
                fontext.setEndPage(i);
                Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
                fontext.writeText(document, dummy);
                List<PdfemleBean> charList = GetCharLocationAndSize.charList;
                // TODO 图片读取
                PDPage pdPage = document.getPage(i-1);
                image.processPage(pdPage);
                List<PdfemleBean> imglist = GetImageLocationsAndSize.imglist;
                List<PdfemleBean> newimglist = new ArrayList<PdfemleBean>();
                List<PdfemleBean> all = new ArrayList<PdfemleBean>();
                // TODO 图片流写入，对图片名称命名,将图片相关信息写入到Fontchar属性里
                for (int im = 0; im < imglist.size(); im++) {
                    PdfemleBean imageBean = imglist.get(im);
                    String imgName = pn+ "_" + i + "_" + im + ".png";
                    //file="HDA0000382884980000011.TIF" wi="700" he="681"
                    imageBean.setFontchar("<img file=\"" + imgName + "\" wi=\"" + imglist.get(im).getWidth()+ "\" he=\"" + imglist.get(im).getHeight() + "\"/>");
                    FileOutputStream out = new FileOutputStream("D:\\user\\newxml\\" + imgName);
                    ImageIO.write(imageBean.getImages(), "png", out);
                    out.close();
                    newimglist.add(imageBean);
                }
                // TODO 图片，文字添加到同一个list中，并对list进行y坐标排序
                all.addAll(charList);
                all.addAll(newimglist);
                Collections.sort(all);
                float point_y = 800;
                float height = 0;
                // TODO 通过坐标y进行排序 height=5.32875为[0001]  height=6.018 小于6.018的为文字 height=7.105 图片
                // TODO 通过y坐标确定在同一行，加入行分隔符
                List<PdfemleBean> lines = new ArrayList<PdfemleBean>();
                for (int j = 0; j < all.size(); j++) {
                    PdfemleBean textBean = all.get(j);
                    float y = textBean.getPoint_y();
                    String chenck = textBean.getChenck();
                    if(chenck.equals("文字")){
                        if(y+10>=point_y&&y-10<=point_y){
                            lines.add(all.get(j));
                        }else{
                            point_y=all.get(j).getPoint_y();
                            height = all.get(j).getHeight();
                            PdfemleBean pdfemleBean = new PdfemleBean();
                            pdfemleBean.setFontchar("\u007F");
                            lines.add(pdfemleBean);
                            lines.add(all.get(j));
                        }
                    }else{
                        float heightimg=all.get(j).getScale_y();
                        if((y+heightimg/2)+10>=point_y+height/2&&(y+heightimg/2)-10<=point_y+height/2){
                            lines.add(all.get(j));
                        }else{
                            point_y=all.get(j).getPoint_y();
                            PdfemleBean pdfemleBean = new PdfemleBean();
                            pdfemleBean.setFontchar("\u007F");
                            lines.add(pdfemleBean);
                            lines.add(all.get(j));

                        }
                    }
                }
                List<PdfemleBean> lined = new ArrayList<PdfemleBean>();
                List<List<PdfemleBean>> pdf = new ArrayList<List<PdfemleBean>>();
                // todo 分成行
                for (PdfemleBean line : lines) {
                    String fontchar = line.getFontchar();
                    if (fontchar.equals("\u007F")) {
                        pdf.add(lined);
                        lined = new ArrayList<PdfemleBean>();
                    } else {
                        lined.add(line);
                    }
                }
                pdf.add(lined);
                // todo 根据x判断前后顺序,按行进行处理
                int claimPage =0;
                int decPage= 0;
                int decimgPage= 0;

                for (List<PdfemleBean> pdfemleBeans1 : pdf) {
                    pdfemleBeans1.sort(new Comparator<PdfemleBean>() {
                        @Override
                        public int compare(PdfemleBean o1, PdfemleBean o2) {
                            return Float.compare(o1.getPoint_x(), o2.getPoint_x());
                        }
                    });
                    StringBuilder ll = new StringBuilder();
                    for (PdfemleBean pdfemleBean : pdfemleBeans1) {
                        if ((pdfemleBean.getPoint_x() > 69 && pdfemleBean.getChenck().equals("文字")) || (pdfemleBean.getChenck().equals("图片"))) {
                            ll.append(pdfemleBean.getFontchar());
                        }
                    }
                    if (i == 1) {
                        if (ll.toString().startsWith("(10)申请公布号") || ll.toString().contains("(10)授权公告号")) {
                            patent_pn = RegexUtil.getPn(ll.toString().replace(" ", ""));
                        }
                        if (ll.toString().startsWith("(43)申请公布日")) {
                            if (pub_date.isEmpty()) {
                                pub_date = RegexUtil.getDate(ll.toString()).replace(".", "-");
                            } else {
                                continue;
                            }
                        }
                        if (ll.toString().contains("(45)授权公告日")) {
                            if (pub_date.isEmpty()) {
                                pub_date = RegexUtil.getDate(ll.toString()).replace(".", "-");
                            } else {
                                continue;
                            }
                        }
                        if (ll.toString().startsWith("(21)申请号")) {
                            patent_an = RegexUtil.getAn(ll.toString());
                        }
                        if (ll.toString().startsWith("(22)申请日")) {
                            app_date = RegexUtil.getDate(ll.toString()).replace(".", "-");
                        }
                    }
                    if (ll.toString().contains("权　利　要　求　书") && ll.toString().startsWith("CN") && ll.toString().endsWith("页")) {
                        claimPage = i;
                        String point = String.valueOf(numClaim).substring(1, 5);
                        if (ll.toString().contains("权　利　要　求　书1/") && ll.toString().startsWith("CN") && ll.toString().endsWith("页")) {
                            bf.append("<cn-bibliographic-data>\n<cn-publication-reference>\n<document-id>\n")
                                    .append("<country>").append(RegexUtil.getCountry(patent_pn))
                                    .append("</country>\n<doc-number>").append(RegexUtil.getNumber(patent_pn))
                                    .append("</doc-number>\n<kind>").append(RegexUtil.getkind(patent_pn))
                                    .append("</kind>\n").append("<date>").append(pub_date).append("</date>\n")
                                    .append("</document-id>\n</cn-publication-reference>\n<application-reference>\n<document-id>\n<country>CN</country>\n<doc-number>")
                                    .append(patent_an).append("</doc-number>\n").append("<date>").append(app_date).append("</date>\n")
                                    .append("</document-id>\n</application-reference>\n</cn-bibliographic-data>\n<application-body lang=\"CN\" country=\"CN\">\n<claims>\n<claim id=\"cl")
                                    .append(point).append("\" num=\"")
                                    .append(point).append("\">\n");
                        } else {
                            continue;
                        }
                        continue;
                    }
                    // TODO 找出权利要求对应的页数，然后针对权利要求进行xml拼接
                    if (claimPage > 0) {
                        if (ll.toString().endsWith("。")) {
                            numClaim++;
                            String point = String.valueOf(numClaim).substring(1, 5);
                            bf.append("<claim-text>").append(ll).append("</claim-text>\n");
                            bf.append("</claim>\n<claim id=\"cl")
                                    .append(point).append("\" num=\"")
                                    .append(point).append("\">\n");
                        } else {
                            String claimtext = "<claim-text>" + ll + "</claim-text>\n";
                            bf.append(claimtext);
                        }

                    }
                    if (ll.toString().contains("说　明　书") && ll.toString().startsWith("CN") && ll.toString().endsWith("页") && !ll.toString().contains("附　图")) {
                        decPage = i;
                        if (ll.toString().contains("说　明　书1/") && ll.toString().startsWith("CN") && ll.toString().endsWith("页") && !ll.toString().contains("附　图")) {
                            String de = "<claim id=\"cl" + String.valueOf(numClaim).substring(1, 5) + "\" num=\"" + String.valueOf(numClaim).substring(1, 5) + "\">";
                            String replace = bf.toString().replace(de, "").trim();
                            bf = new StringBuffer();
                            bf.append(replace);
                            bf.append("\n</claims>\n<description>\n");
                        } else {
                            continue;
                        }
                        continue;
                    }
                    if (ll.toString().contains("说　明　书") && ll.toString().startsWith("CN") && ll.toString().endsWith("页") && ll.toString().contains("附　图")) {
                        decimgPage = i;
                        continue;
                    }
                    // TODO 找出说明书对应的页数，然后针对权利要求进行xml拼接
                    if (decPage > 0) {
                        desList.add(ll.toString());
                    }
                    // TODO 找出说明书附图对应的页数，然后针对权利要求进行xml拼接
                    if (decimgPage > 0) {
                        desimgList.add(ll.toString());
                    }

                }
                charList.clear();
                imglist.clear();
            }
            // 拼接说明书xml结构
            int desc_tf = 0;
            int desc_ba = 0;
            int desc_disc = 0;
            int desc_mode = 0;
            int desc_draw = 0;
            int numDesc = 10001;
            for (int i = 0; i < desList.size(); i++) {
                String line = desList.get(i);
                if(line.equals("技术领域")) desc_tf=i;
                if(line.equals("背景技术")) desc_ba=i;
                if(line.equals("发明内容")) desc_disc=i;
                if(line.equals("附图说明")) desc_draw=i;
                if(line.equals("具体实施方式")) desc_mode=i;
            }
            for (int i = 0; i < desList.size(); i++) {
                String line = desList.get(i);
                if(i<desc_tf){
                    if(desc_tf==1){
                        bf.append("<invention-title id=\"tilte1\">")
                        .append(line);
                    }else{
                        if(i==0){
                            bf.append("<invention-title id=\"tilte1\">").append(line);
                        }else{
                            bf.append(line);
                        }
                    }
                }else if(i>=desc_tf&&i<desc_ba){
                    if(i==desc_tf){
                        String num = String.valueOf(numDesc).substring(1, 5);
                        bf.append("</invention-title>\n<technical-field>\n<p id=\"p")
                        .append(num).append("\" num=\"").append(num).append("\">")
                        .append(line)
                        .append("<br/>");
                    }else{
                        Pattern pattern = Pattern.compile("^\\[[0-9].*\\]");
                        Matcher matcher = pattern.matcher(line);
                        if(matcher.find())
                        {
                            numDesc++;
                            bf.append("</p>\n<p id=\"p")
                                    .append(String.valueOf(numDesc), 1, 5).append("\" num=\"")
                                    .append(String.valueOf(numDesc), 1, 5).append("\">")
                                    .append(line.replace(matcher.group(),""));
                        }else{
                            bf.append(line).append("<br/>");
                        }
                    }
                }else if(i>=desc_ba&&i<desc_disc){
                    if(i==desc_ba){
                        numDesc++;
                        bf.append("<br/></p>\n</technical-field>\n<background-art>\n<p id=\"p")
                                .append(String.valueOf(numDesc), 1, 5).append("\" num=\"").append(String.valueOf(numDesc), 1, 5).append("\">")
                                .append(line)
                                .append("<br/>");
                    }else{
                        Pattern pattern = Pattern.compile("^\\[[0-9].*\\]");
                        Matcher matcher = pattern.matcher(line);
                        if(matcher.find())
                        {
                            numDesc++;
                            bf.append("</p>\n<p id=\"p")
                                    .append(String.valueOf(numDesc), 1, 5).append("\" num=\"")
                                    .append(String.valueOf(numDesc), 1, 5).append("\">")
                                    .append(line.replace(matcher.group(),""));
                        }else{
                            bf.append(line).append("<br/>");
                        }
                    }
                }else if(i>=desc_disc&&i<desc_draw){
                     if(i==desc_disc){
                        numDesc++;
                        bf.append("</p>\n</background-art>\n<disclosure>\n<p id=\"p")
                                .append(String.valueOf(numDesc), 1, 5).append("\" num=\"").append(String.valueOf(numDesc), 1, 5).append("\">")
                                .append(line)
                                .append("<br/>");
                    }else{
                         Pattern pattern = Pattern.compile("^\\[[0-9].*\\]");
                         Matcher matcher = pattern.matcher(line);
                         if(matcher.find())
                         {
                             numDesc++;
                             bf.append("</p>\n<p id=\"p")
                                     .append(String.valueOf(numDesc), 1, 5).append("\" num=\"")
                                     .append(String.valueOf(numDesc), 1, 5).append("\">")
                                     .append(line.replace(matcher.group(),""));
                         }else{
                             bf.append(line).append("<br/>");
                         }
                     }
                }else if(i>=desc_draw&&i<desc_mode){
                    if(i==desc_draw){
                        numDesc++;
                        bf.append("</p>\n</disclosure>\n<description-of-drawings>\n<p id=\"p")
                                .append(String.valueOf(numDesc), 1, 5).append("\" num=\"").append(String.valueOf(numDesc), 1, 5).append("\">")
                                .append(line)
                                .append("<br/>");
                    }else{
                        Pattern pattern = Pattern.compile("^\\[[0-9].*\\]");
                        Matcher matcher = pattern.matcher(line);
                        if(matcher.find())
                        {
                            numDesc++;
                            bf.append("</p>\n<p id=\"p")
                                    .append(String.valueOf(numDesc), 1, 5).append("\" num=\"")
                                    .append(String.valueOf(numDesc), 1, 5).append("\">")
                                    .append(line.replace(matcher.group(),""));
                        }else{
                            bf.append(line).append("<br/>");
                        }
                    }
                }else{
                    if(i==desc_mode){
                        numDesc++;
                        bf.append("</p>\n</description-of-drawings>\n<mode-for-invention>\n<p id=\"p")
                                .append(String.valueOf(numDesc), 1, 5).append("\" num=\"").append(String.valueOf(numDesc), 1, 5).append("\">")
                                .append(line)
                                .append("<br/>");
                    }else{
                        Pattern pattern = Pattern.compile("^\\[[0-9].*\\]");
                        Matcher matcher = pattern.matcher(line);
                        if(matcher.find())
                        {
                            numDesc++;
                            bf.append("</p>\n<p id=\"p")
                                    .append(String.valueOf(numDesc), 1, 5).append("\" num=\"")
                                    .append(String.valueOf(numDesc), 1, 5).append("\">")
                                    .append(line.replace(matcher.group(),""));
                        }else{
                            bf.append(line).append("<br/>");
                        }
                    }
                }
            }
            bf.append("</p>\n</mode-for-invention>\n</description>\n<drawings>\n");
            int imgid = 10000;
            for (int i = 0; i < desimgList.size(); i++) {
                String line = desimgList.get(i);
                if(line.startsWith("图")){
                    String img = desimgList.get(i-1);
                    imgid++;
                    bf.append("<figure id=\"f").append(String.valueOf(imgid), 1, 5).append("\" num=\"").append(String.valueOf(imgid), 1, 5)
                            .append("\" figure-labels=\"")
                            .append(line)
                            .append("\">\n");
                    bf.append(img);
                    bf.append("\n</figure>\n");
                }
            }
            bf.append("</drawings>\n</application-body>\n</cn-patent-document>");
//            System.out.println(bf);
            FileOutputStream out = new FileOutputStream("D:\\user\\newxml\\" + patent_pn + ".xml");
            out.write(bf.toString().getBytes());
            out.close();
            //关闭 pdf流
            document.close();
        }
    }
}
