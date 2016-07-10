package com.accounts.rb.service.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.accounts.rb.domain.Dealer;
import com.accounts.rb.domain.Imei;
import com.accounts.rb.domain.Invoice;
import com.accounts.rb.domain.InvoiceItem;
import com.accounts.rb.domain.InvoiceType;
import com.accounts.rb.domain.Profile;
import com.accounts.rb.repository.DealerRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

@Service
public class PdfService {
  
  @Inject
  DealerRepository dealerRepository;
  
  private static String DATE_FORMAT = "MM/dd/yyyy";
  private static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
  private static Font blackHeadingFont = FontFactory.getFont(FontFactory.COURIER, 24, Font.BOLD);
  private static Font blackHeadingLargeFont = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD);
  private static Font blackBoldFont = FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD);
  private static Font blackFont = FontFactory.getFont(FontFactory.COURIER, 10);
  private static Font newFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
  

  public File createPdfInvoice(Invoice invoice, Profile profile) throws IOException {
    // create output file
    File pdfFile = File.createTempFile("inv", ".pdf");
    
    Document document = new Document();
    Dealer dealer = dealerRepository.findOne(invoice.getDealerId());
    try {
        // URL url = getClass().getClassLoader().getResource("Invoice1.pdf");
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        // Paragraph with color and font styles
        Paragraph paragraphOne = new Paragraph(profile.getFirmName().toUpperCase(), blackHeadingFont);
        paragraphOne.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraphOne);

        Paragraph paragraphTwo = new Paragraph(
                "\n " + (StringUtils.isNotBlank(profile.getAddress().getAddress1()) ? profile.getAddress().getAddress1() : "") 
                        + ", " + (StringUtils.isNotBlank(profile.getAddress().getAddress2()) ? profile.getAddress().getAddress2() : "") + "\n"
                        + (StringUtils.isNotBlank(profile.getAddress().getCity()) ? profile.getAddress().getCity() : "" ) + " - " 
                        + profile.getAddress().getPincode() + "\n Phone : "
                        + (StringUtils.isNotBlank(profile.getAddress().getPhone()) ? profile.getAddress().getPhone() : "") + "\n" 
                        + (StringUtils.isNotBlank(profile.getAddress().getEmail()) ? profile.getAddress().getEmail() : "" ));
        paragraphTwo.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraphTwo);

        Paragraph paragraphAuth = new Paragraph("(Pre Authenticated By)", newFont);
        paragraphAuth.setAlignment(Element.ALIGN_RIGHT);
        document.add(paragraphAuth);
        
        Paragraph paragraphThree = null;
        switch (InvoiceType.valueOf(invoice.getInvoiceType())) {
          case TAX_INVOICE: {
            paragraphThree = new Paragraph("TAX INVOICE", blackHeadingLargeFont);
            break;
          }
          case SALES_INVOICE: {
            paragraphThree = new Paragraph("SALES INVOICE", blackHeadingLargeFont);
            break;
          }
          case SAMPLE_INVOICE: {
            paragraphThree = new Paragraph("SAMPLE INVOICE", blackHeadingLargeFont);
            break;
          }
        }
        paragraphThree.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraphThree);
        LineSeparator ls = new LineSeparator();
        document.add(new Chunk(ls));

        Paragraph paragraphFour = new Paragraph("TIN No. : " + (StringUtils.isNotBlank(profile.getTin()) ? profile.getTin() : ""));
        paragraphFour.setAlignment(Element.ALIGN_RIGHT);
        document.add(paragraphFour);
        document.add(new Chunk(ls));

        Paragraph paragraphFive = new Paragraph(StringUtils.isNotBlank(dealer.getFirmName()) ? dealer.getFirmName().toUpperCase() : "", blackBoldFont);
        paragraphFive.add("\t\t\t\tInvoice No.:");
        paragraphFive.add(StringUtils.isNotBlank(invoice.getInvoiceNumber()) ? invoice.getInvoiceNumber() : "");
        paragraphFive.setFont(blackBoldFont);
        paragraphFive.add("\t\t\t\tDate : " + sdf.format(new Date()));
        document.add(paragraphFive);

        Paragraph paragraphSix = new Paragraph("\n " + (StringUtils.isNotBlank(dealer.getAddress().getAddress1()) ? dealer.getAddress().getAddress1() : "") + ", "
                + (StringUtils.isNotBlank(dealer.getAddress().getAddress2()) ? dealer.getAddress().getAddress2() : "") + "\n" 
                + (StringUtils.isNotBlank(dealer.getAddress().getCity()) ? dealer.getAddress().getCity() : "") + " - "
                + dealer.getAddress().getPincode() 
                + "\n\n MOB- " + (StringUtils.isNotBlank(dealer.getAddress().getPhone()) ? dealer.getAddress().getPhone() : "") + "\n EMAIL- "
                + (StringUtils.isNotBlank(dealer.getAddress().getEmail()) ? dealer.getAddress().getEmail() : "") + "\n TIN. No.- " 
                + (StringUtils.isNotBlank(dealer.getTin()) ? dealer.getTin() : ""), blackFont);
        paragraphSix.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraphSix);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100); // Width 100%
        table.setSpacingBefore(10f); // Space before table
        table.setSpacingAfter(10f); // Space after table

        float[] columnWidths = { 0.7f, 3f, 0.6f, 0.6f, 0.6f, 0.8f };
        table.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Paragraph("S.No.", blackBoldFont));
        cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell1.setPaddingLeft(10);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell2 = new PdfPCell(new Paragraph("Item Description", blackBoldFont));
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell2.setPaddingLeft(10);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        /*PdfPCell cell3 = new PdfPCell(new Paragraph("M.R.P.", blackBoldFont));
        cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell3.setPaddingLeft(10);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);*/

        PdfPCell cell4 = new PdfPCell(new Paragraph("Qty.", blackBoldFont));
        cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell4.setPaddingLeft(10);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell5 = new PdfPCell(new Paragraph("Rate", blackBoldFont));
        cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell5.setPaddingLeft(10);
        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell6 = new PdfPCell(new Paragraph("Tax", blackBoldFont));
        cell6.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell6.setPaddingLeft(10);
        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell7 = new PdfPCell(new Paragraph("Amount", blackBoldFont));
        cell7.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell7.setPaddingLeft(10);
        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell7.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(cell1);
        table.addCell(cell2);
        //table.addCell(cell3);
        table.addCell(cell4);
        table.addCell(cell5);
        table.addCell(cell6);
        table.addCell(cell7);
        table.setHeaderRows(1);

        List<TableData> td = new ArrayList<TableData>();
        List<InvoiceItem> currInvoiceItems = invoice.getInvoiceItems();
        int invoiceCount = 1;
        for (InvoiceItem invoiceItem : currInvoiceItems) {
            td.add(new TableData(String.valueOf(invoiceCount), 
                    invoiceItem.getProductName() + " " + (StringUtils.isNotBlank(invoiceItem.getColor()) ? invoiceItem.getColor() : ""),
                    String.valueOf(invoiceItem.getMrp()), String.valueOf(invoiceItem.getQuantity()),
                    String.valueOf(invoiceItem.getAmount()), 
                    StringUtils.isNotBlank(invoiceItem.getTaxType()) ? invoiceItem.getTaxType() + " " + invoiceItem.getTaxRate() + " %" : ""));
            invoiceCount++;
        }

        int count = 1;
        for (TableData tableData : td) {

            PdfPCell cell = new PdfPCell(
                    new Phrase(tableData.getSno(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            if (count != td.size()) {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            } else {
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
            }
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(tableData.getItemdesc(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
            setCellBorder(td, count, cell);
            table.addCell(cell);
            /*cell = new PdfPCell(new Phrase(tableData.getMrp(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
            setCellBorder(td, count, cell);
            table.addCell(cell);*/
            cell = new PdfPCell(new Phrase(tableData.getQty(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
            setCellBorder(td, count, cell);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(tableData.getMrp(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
            setCellBorder(td, count, cell);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(tableData.getTaxRate() , FontFactory.getFont(FontFactory.HELVETICA, 8)));
            setCellBorder(td, count, cell);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(tableData.getRate(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
            setCellBorder(td, count, cell);
            table.addCell(cell);
            count++;
        }

        document.add(table);
        
        PdfPTable tableAmount = new PdfPTable(2);
        tableAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tableAmount.setWidthPercentage(30);
        tableAmount.setSpacingBefore(10f); // Space before table
        tableAmount.setSpacingAfter(10f); // Space after table

        String[] str = { "SUB TOTAL", "Taxes" , "Shipping Charges" ,"Adjustments" };
        BigDecimal[] amounts = { invoice.getSubtotal(), invoice.getTaxes(), invoice.getShippingCharges(), invoice.getAdjustments() };
        for (int i = 0; i < 4; i++) {
            PdfPCell cellOne = new PdfPCell(new Phrase(str[i], newFont));
            PdfPCell cellTwo = new PdfPCell(new Phrase(String.format("%.2f", amounts[i]), newFont));
            cellOne.setBorder(Rectangle.NO_BORDER);
            cellOne.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellOne.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTwo.setBorder(Rectangle.NO_BORDER);
            cellTwo.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellTwo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tableAmount.addCell(cellOne);
            tableAmount.addCell(cellTwo);
        }

        document.add(tableAmount);
        document.add(new Chunk(ls));

        Paragraph paragraphEight = new Paragraph(
                "Rs. " + EnglishNumberToWords.convert(invoice.getTotalAmount().intValue()) + " Only", newFont);
        document.add(paragraphEight);

        PdfPTable tableGrand = new PdfPTable(2);
        tableGrand.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tableGrand.setWidthPercentage(30);
        tableGrand.setSpacingBefore(10f); // Space before table
        tableGrand.setSpacingAfter(10f); // Space after table
        
        PdfPCell cellOne = new PdfPCell(new Phrase("GRAND TOTAL", newFont));
        PdfPCell cellTwo = new PdfPCell(new Phrase(String.format("%.2f", invoice.getTotalAmount()), newFont));
        cellOne.setBorder(Rectangle.NO_BORDER);
        cellOne.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellOne.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellTwo.setBorder(Rectangle.NO_BORDER);
        cellTwo.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cellTwo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableGrand.addCell(cellOne);
        tableGrand.addCell(cellTwo);
        document.add(tableGrand);
        
        document.add(new Chunk(ls));

        Paragraph paragraph = new Paragraph("Terms & Conditions", newFont);
        document.add(paragraph);
        paragraph = new Paragraph("ALL DISPUTES SUBJECTED TO NOIDA JURISDICTION ONLY", newFont);
        document.add(paragraph);

        paragraph = new Paragraph("\n\n\nFor RINGING BELLS PRIVATE LIMITED\n\n\n", blackBoldFont);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(paragraph);
        paragraph = new Paragraph("Authorised Signatory", newFont);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(paragraph);

        boolean imeiExits = true;
        PdfPTable table2 = new PdfPTable(2);
        for (InvoiceItem invoiceItem : currInvoiceItems) {
            if (!CollectionUtils.isEmpty(invoiceItem.getImeis())) {
                if (imeiExits) {
                    document.newPage();
                    table2.setWidthPercentage(100); // Width 100%
                    table2.setSpacingBefore(10f); // Space before table
                    table2.setSpacingAfter(10f); // Space after table

                    PdfPCell cell21 = new PdfPCell(new Paragraph("IMEI1", blackBoldFont));
                    cell21.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell21.setPaddingLeft(10);
                    cell21.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell21.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    PdfPCell cell22 = new PdfPCell(new Paragraph("IMEI2", blackBoldFont));
                    cell22.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell22.setPaddingLeft(10);
                    cell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell22.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    table2.addCell(cell21);
                    table2.addCell(cell22);
                    imeiExits = false;
                }
                for (Imei imeiNum : invoiceItem.getImeis()) {
                    PdfPCell cell = new PdfPCell(
                            new Phrase(imeiNum.getImei1(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                    table2.addCell(cell);
                    cell = new PdfPCell(
                            new Phrase(imeiNum.getImei2(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                    table2.addCell(cell);
                }
            }
        }
        document.add(table2);
        document.close();
        writer.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return pdfFile;
  }
  
    private static void setCellBorder(List<TableData> td, int count, PdfPCell cell) {
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      if (count != td.size()) {
        cell.setBorder(Rectangle.RIGHT);
      } else {
        cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
      }
    }
}
