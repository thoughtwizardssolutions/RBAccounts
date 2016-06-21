package com.accounts.rb.web.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accounts.rb.domain.Invoice;
import com.accounts.rb.service.InvoiceService;
import com.accounts.rb.web.rest.util.HeaderUtil;
import com.accounts.rb.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
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
import com.rb.accounts.service.pdf.EnglishNumberToWords;
import com.rb.accounts.service.pdf.TableData;

/**
 * REST controller for managing Invoice.
 */
@RestController
@RequestMapping("/api")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);
        
    @Inject
    private InvoiceService invoiceService;
    
    /**
     * POST  /invoices : Create a new invoice.
     *
     * @param invoice the invoice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoice, or with status 400 (Bad Request) if the invoice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invoices",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) throws URISyntaxException {
        log.debug("REST request to save Invoice : {}", invoice);
        if (invoice.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("invoice", "idexists", "A new invoice cannot already have an ID")).body(null);
        }
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        invoice.setCreatedBy(user.getUsername());
        Invoice result = invoiceService.save(invoice);
        return ResponseEntity.created(new URI("/api/invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("invoice", result.getId().toString()))
            .body(result);
    }
    
    /**
     * POST  /invoices/pdf : Create a new invoice PDF.
     *
     * @param invoice the invoice to create PDF for
     * @return 
     * @return attachment
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pdf",
        method = RequestMethod.POST,
        produces = "application/pdf")
    @Timed
    public ResponseEntity<InputStreamResource> createInvoicePdf(@Valid @RequestBody Invoice invoice, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException,FileNotFoundException, IOException {
        log.debug("REST request to save Invoice : {}", invoice);
        Font blackHeadingFont = FontFactory.getFont(FontFactory.COURIER, 24, Font.BOLD);
        Font blackHeadingLargeFont = FontFactory.getFont(FontFactory.COURIER, 18, Font.BOLD);
        Font blackBoldFont = FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD);
        Font blackFont = FontFactory.getFont(FontFactory.COURIER, 10);
        Font newFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
        String DATE_FORMAT = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Document document = new Document();
        double amount = 0;
        double totalAmount = 0;
        try {
        	URL url = getClass().getClassLoader().getResource("Invoice1.pdf");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(url.getFile())));
            document.open();
            // document.add(new Paragraph("Styling Example"));

            // Paragraph with color and font styles
            Paragraph paragraphOne = new Paragraph("RINGING BELLS PRIVATE LIMITED", blackHeadingFont);
            paragraphOne.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraphOne);

            Paragraph paragraphTwo = new Paragraph("\n B-44, IInd Floor, SECTOR-63 \n NOIDA-201301 "
                    + "\n Phone : 0120-4313097 \n ww.ringingbells.co.in");
            paragraphTwo.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraphTwo);

            Paragraph paragraphThree = new Paragraph("TAX INVOICE", blackHeadingLargeFont);
            paragraphThree.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraphThree);
            LineSeparator ls = new LineSeparator();
            document.add(new Chunk(ls));

            Paragraph paragraphFour = new Paragraph("TIN No. : 09765723141");
            paragraphFour.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraphFour);
            document.add(new Chunk(ls));

            Paragraph paragraphFive = new Paragraph("M/S PIONNER ENTERPRISE", blackBoldFont);
            paragraphFive.add("\t\t\t\tInvoice No.:");
            paragraphFive.add("T000079");
            paragraphFive.setFont(blackBoldFont);
            paragraphFive.add("\t\t\t\tDate : " + sdf.format(new Date()));
            document.add(paragraphFive);

            Paragraph paragraphSix = new Paragraph(
                    "\n H. OFFICE 232, MOHALLA JATKATIAN TALAB, \n KHATI KAN, LAKHDATA BAZAR, JAMMU "
                            + "\n\n MOB-9419193976 \n EMAIL- TEST@GMAIL.COM \n TIN.No.: 01791021207",
                    blackFont);
            paragraphSix.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraphSix);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100); // Width 100%
            table.setSpacingBefore(10f); // Space before table
            table.setSpacingAfter(10f); // Space after table

            float[] columnWidths = { 0.7f, 3f, 0.8f, 0.6f, 0.6f, 0.6f, 0.8f };
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

            PdfPCell cell3 = new PdfPCell(new Paragraph("M.R.P.", blackBoldFont));
            cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell3.setPaddingLeft(10);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

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

            PdfPCell cell6 = new PdfPCell(new Paragraph("Vat%", blackBoldFont));
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
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
            table.addCell(cell6);
            table.addCell(cell7);
            table.setHeaderRows(1);

            List<TableData> td = new ArrayList<TableData>();
            td.add(new TableData("1", "SMART 101 WHITE", "0.00", "15", "2313.50"));
            td.add(new TableData("2", "SMART 101 BLACK", "0.00", "15", "2313.50"));
            int count = 1;
            for (TableData tableData : td) {
                
                PdfPCell cell = new PdfPCell(new Phrase(tableData.getSno(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
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
                cell = new PdfPCell(new Phrase(tableData.getMrp(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
                setCellBorder(td, count, cell);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(tableData.getQty(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
                setCellBorder(td, count, cell);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(tableData.getRate(), FontFactory.getFont(FontFactory.HELVETICA, 8)));
                setCellBorder(td, count, cell);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("2.00", FontFactory.getFont(FontFactory.HELVETICA, 8)));
                setCellBorder(td, count, cell);
                table.addCell(cell);
                amount = 0;
                amount = Integer.parseInt(tableData.getQty()) * Double.parseDouble(tableData.getRate());
                totalAmount += amount;
                cell = new PdfPCell(new Phrase(String.valueOf(amount), FontFactory.getFont(FontFactory.HELVETICA, 8)));
                setCellBorder(td, count, cell);
                table.addCell(cell);
                count++;
            }
            
            document.add(table);
            
            Paragraph paragraphSeven = new Paragraph("SUB TOTAL", newFont);
            paragraphSeven.add("\t\t" + totalAmount);
            paragraphSeven.add("\n CST 2%");
            paragraphSeven.add("\t\t" + String.format("%.2f", (totalAmount * 0.02)));
            paragraphSeven.add("\n Roundoff");
            paragraphSeven.add("\t\t0.10");
            paragraphSeven.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraphSeven);
            document.add(new Chunk(ls));
            
            Paragraph paragraphEight = new Paragraph("Rs. " + EnglishNumberToWords.convert(new Double(totalAmount).intValue()) + " Only", newFont);
            document.add(paragraphEight);
            
            paragraphEight = new Paragraph("GRAND TOTAL\t\t" + totalAmount, newFont);
            paragraphEight.setAlignment(Element.ALIGN_RIGHT);
            document.add(paragraphEight);
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
            
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/pdf");
        /*PrintWriter out = response.getWriter();
        String filename = "12354564.pdf";
        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");*/

        // use inline if you want to view the content in browser, helpful for
        // pdf file
        // response.setHeader("Content-Disposition","inline; filename=\"" +
        // filename + "\"");
        /*FileInputStream fileInputStream = new FileInputStream(filename);
        int i;
        while ((i = fileInputStream.read()) != -1) {
            out.write(i);
        }
        out.close();*/
        ClassPathResource pdfFile = new ClassPathResource("Invoice1.pdf");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(pdfFile.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(pdfFile.getInputStream()));
        
    }

    private static void setCellBorder(List<TableData> td, int count, PdfPCell cell) {
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		if (count != td.size()) {
			cell.setBorder(Rectangle.RIGHT);
		} else {
			cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
		}
	}
    
    /**
     * PUT  /invoices : Updates an existing invoice.
     *
     * @param invoice the invoice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoice,
     * or with status 400 (Bad Request) if the invoice is not valid,
     * or with status 500 (Internal Server Error) if the invoice couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invoices",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invoice> updateInvoice(@Valid @RequestBody Invoice invoice) throws URISyntaxException {
        log.debug("REST request to update Invoice : {}", invoice);
        if (invoice.getId() == null) {
            return createInvoice(invoice);
        }
        Invoice result = invoiceService.save(invoice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("invoice", invoice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoices : get all the invoices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoices in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/invoices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Invoice>> getAllInvoices(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Invoices");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Invoice> page = invoiceService.findByCreatedBy(pageable, user.getUsername());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /invoices/:id : get the "id" invoice.
     *
     * @param id the id of the invoice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoice, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/invoices/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        log.debug("REST request to get Invoice : {}", id);
        Invoice invoice = invoiceService.findOne(id);
        return Optional.ofNullable(invoice)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /invoices/:id : delete the "id" invoice.
     *
     * @param id the id of the invoice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/invoices/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        log.debug("REST request to delete Invoice : {}", id);
        invoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("invoice", id.toString())).build();
    }

}
