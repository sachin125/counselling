package com.inn.counselling.utils;

import com.inn.counselling.constant.ResourceConstant;

public class ResourceUtil implements ResourceConstant {
	
	public String getType(String filename){
		String fileExtension="";
		char ch='.';
		int dot = filename.lastIndexOf(ch);
		if(dot>0){
			fileExtension = filename.substring(dot+1,filename.length());				
		}
		if(fileExtension!=null) {
			if(TXT.equalsIgnoreCase(fileExtension)){
				return TEXT_DOCUEMNT;
			}else if(RTF.equalsIgnoreCase(fileExtension)){
				return RICH_TEXT_FILES;
			}else if(DOC.equalsIgnoreCase(fileExtension) || DOCX.equalsIgnoreCase(fileExtension) || ODT.equalsIgnoreCase(fileExtension)){
				return WORD_DOCUMENT;
			}else if(XLS.equalsIgnoreCase(fileExtension) || XLSX.equalsIgnoreCase(fileExtension)){
				return SPREADSHEET;
			}else if(PPT.equalsIgnoreCase(fileExtension) || PPTX.equalsIgnoreCase(fileExtension)){
				return POWERPOINT_PRESENTATION;
			}else if(PPS.equalsIgnoreCase(fileExtension) || PPSX.equalsIgnoreCase(fileExtension)|| PPTM.equalsIgnoreCase(fileExtension) ){
				return PRESENTATION;
			}else if(ODP.equalsIgnoreCase(fileExtension)){
				return OPEN_DOCUMENT_PRESENTATION;
			}else if(EMF.equalsIgnoreCase(fileExtension)){
				return ENHANCED_WINDOW_METAFILE;
			}else if(PDF.equalsIgnoreCase(fileExtension)){
				return ADOBE_PDF;
			}else if(KEY.equalsIgnoreCase(fileExtension)){
				return KEYNOTE_PRESENTATION;
			}else if(NUMBERS.equalsIgnoreCase(fileExtension)){
				return NUMBERS;
			}else if(HTM.equalsIgnoreCase(fileExtension)|| HTML.equalsIgnoreCase(fileExtension)){
				return WEB_PAGES;
			}else if(BLANK.equalsIgnoreCase(fileExtension)){
				return BLANK;
			}else if(PSD.equalsIgnoreCase(fileExtension) || AI.equalsIgnoreCase(fileExtension) || TIFF.equalsIgnoreCase(fileExtension) || TIF.equalsIgnoreCase(fileExtension) || JPEG.equalsIgnoreCase(fileExtension)||JPG.equalsIgnoreCase(fileExtension)||GIF.equalsIgnoreCase(fileExtension)|| PNG.equalsIgnoreCase(fileExtension)){
				return IMAGE;
			}else if(BMP.equalsIgnoreCase(fileExtension)){
				return DEVICE_INDEPENDENT_BITMAP;
			}else if(WMF.equalsIgnoreCase(fileExtension)){
				return WINDOWS_METAFILE;
			}else if(ACC.equalsIgnoreCase(fileExtension)||WAV.equalsIgnoreCase(fileExtension)||M4A.equalsIgnoreCase(fileExtension)||AIFF.equalsIgnoreCase(fileExtension)||MP3.equalsIgnoreCase(fileExtension)){
				return MUSIC;
			}else if(THREEGP.equalsIgnoreCase(fileExtension)||MKV.equalsIgnoreCase(fileExtension)||M4V.equalsIgnoreCase(fileExtension)||AVI.equalsIgnoreCase(fileExtension)||MP4.equalsIgnoreCase(fileExtension)||MPG.equalsIgnoreCase(fileExtension)||MPG.equalsIgnoreCase(fileExtension)){
				return MOVIE;
			}else if(EXE.equalsIgnoreCase(fileExtension)){
				return EXECUTABLE;
			}else if(CSV.equalsIgnoreCase(fileExtension)){
				return EXCEL;
			}else if(ZZ.equalsIgnoreCase(fileExtension)||GZ.equalsIgnoreCase(fileExtension)||TZ.equalsIgnoreCase(fileExtension)||ZIP.equalsIgnoreCase(fileExtension)||RAR.equalsIgnoreCase(fileExtension)){
				return COMPRESSED;
			}else{
				return OTHER;
			}
		}
		return fileExtension;
	}
	
}
