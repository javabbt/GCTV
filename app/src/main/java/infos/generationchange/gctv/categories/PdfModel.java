package infos.generationchange.gctv.categories;

import infos.generationchange.gctv.models.MainModel;

public class PdfModel extends MainModel {
    private String field_image_de_fond , field_description_article , title , field_pdf_camerleak;

    public PdfModel(String field_image_de_fond, String field_description_article, String title, String field_pdf_camerleak) {
        this.field_image_de_fond = field_image_de_fond;
        this.field_description_article = field_description_article;
        this.title = title;
        this.field_pdf_camerleak = field_pdf_camerleak;
    }

    public String getField_image_de_fond() {
        return field_image_de_fond;
    }

    public void setField_image_de_fond(String field_image_de_fond) {
        this.field_image_de_fond = field_image_de_fond;
    }

    public String getField_description_article() {
        return field_description_article;
    }

    public void setField_description_article(String field_description_article) {
        this.field_description_article = field_description_article;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getField_pdf_camerleak() {
        return field_pdf_camerleak;
    }

    public void setField_pdf_camerleak(String field_pdf_camerleak) {
        this.field_pdf_camerleak = field_pdf_camerleak;
    }
}
