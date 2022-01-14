package com.mongol.encode;

public class MongolianConverter {
	
	private static final int  ISOLATE = 0;
	private static final int  INITIAL = 1;
	private static final int  INTERMEDIATE = 2;
	private static final int  FINAL = 3;
	
	int CurrentStatus = ISOLATE;
	boolean masculine = false;
	boolean feminine = false;
	boolean firstconsonant = false;
	boolean nobreak_202f = false;
	
    public String convert( String srcdoc ) {
    	
     	StringBuffer result = new StringBuffer();
    	
    	StringBuffer docBuffer = new StringBuffer(srcdoc);
    	for(int i=0; i<docBuffer.length(); i++  ) {
    		int currentCh = docBuffer.charAt(i);
    		if( CurrentStatus == ISOLATE ) {
    			masculine = false;
    			feminine = false;
    			firstconsonant = false;
    		}
    		if( CurrentStatus == ISOLATE ) {
    			if( i < docBuffer.length()-1 ) {
    				if( !isFinal(docBuffer, i) ) {
    					CurrentStatus = INITIAL;
    				}
    			}
    		}
    		if( CurrentStatus == INTERMEDIATE ) {
    			if( i < docBuffer.length()-1 ) {
    				if( isFinal(docBuffer, i) ) {
    					CurrentStatus = FINAL;
    				}
    			} else {
					CurrentStatus = FINAL;
    			}
    			
    		}
    		switch ( CurrentStatus ) {
	    		case ISOLATE:
					i = processIsolate(docBuffer, i, result );
					break;
	    		case INITIAL:
					i = processInitial(docBuffer, i, result );
					break;
	    		case INTERMEDIATE:
					i = processIntermediate(docBuffer, i, result );
					break;
	    		case FINAL:
					i = processFinal(docBuffer, i, result );
					break;
    		}
    		while( i > result.length()-1 ){
    			result.append('\uE809');
    		}
    	}
    	  	
    	return result.toString();
    }

    private int processIsolate(StringBuffer docBuffer, int i, StringBuffer result ) {
    	
    	int iResult = i;
		char currentChar = docBuffer.charAt(i);
		if( currentChar==MCodeConstant.UNI202F_NOBREAK){
			result.append( MCodeConstant.UNI202F_NOBREAK);
			nobreak_202f = true;
			CurrentStatus = ISOLATE;
			return iResult;
		}
		
		switch( currentChar ) {
		
			case  '!' :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == '?' ) {
						result.append( MCodeConstant.UNIE81B);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE81C);
				CurrentStatus = ISOLATE;
				break;
				
			case  '?' :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == '!' ) {
						result.append( MCodeConstant.UNIE81A);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE81D);
				CurrentStatus = ISOLATE;
				break;
				
			case  ';' :
				result.append( MCodeConstant.UNIE81E);
				CurrentStatus = ISOLATE;
				break;
				
			case  ',' :
				result.append( MCodeConstant.UNIE81F);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI202F_NOBREAK :
				break;
				
			case  MCodeConstant.UNI1800_BIRGA :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIEA24_BIRGA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIEA25_BIRGA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					} else if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIEA26_BIRGA_VAR3);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					} else if( nextChar == MCodeConstant.UNI180E_MVS ) {
						result.append( MCodeConstant.UNIEA27_BIRGA_VAR4);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					}
				}
				result.append( MCodeConstant.UNIE800_BIRGA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1801_ELLIPSIS:
				result.append( MCodeConstant.UNIE801_ELLIPSIS);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1802_COMMA:
				result.append( MCodeConstant.UNIE802_COMMA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1803_FULLSTOP:
				result.append( MCodeConstant.UNIE803_FULLSTOP);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1804_COLON:
				result.append( MCodeConstant.UNIE804_COLON);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1805_FOURDOT:
				result.append( MCodeConstant.UNIE805_FOURDOT);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1806:
				result.append( MCodeConstant.UNIE806);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1807:
				result.append( MCodeConstant.UNIE807);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1808:
				result.append( MCodeConstant.UNIE808);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1809:
				result.append( MCodeConstant.UNIE809);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180A_NIRUGU:
				result.append( MCodeConstant.UNIE80A_NIRUGU);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI180B_FVS1:
				result.append( MCodeConstant.UNIE80B);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180C_FVS2:
				result.append( MCodeConstant.UNIE80C);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180D_FVS3:
				result.append( MCodeConstant.UNIE80D);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180E_MVS:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI1820_A ) {
						result.append( MCodeConstant.UNIE827_A_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI1821_E ) {
						result.append( MCodeConstant.UNIE82F_E_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				} else {
					result.append( MCodeConstant.UNIE80E);
					CurrentStatus = ISOLATE;
				}
				break;
				
			case  MCodeConstant.UNI1810_ZERO:
				result.append( MCodeConstant.UNIE810_ZERO);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1811_ONE:
				result.append( MCodeConstant.UNI1811_ONE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1812_TWO:
				result.append( MCodeConstant.UNI1812_TWO);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1813_THREE:
				result.append( MCodeConstant.UNIE813_THREE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1814_FOUE:
				result.append( MCodeConstant.UNIE814_FOUR);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1815_FIVE:
				result.append( MCodeConstant.UNIE815_FIVE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1816_SIX:
				result.append( MCodeConstant.UNIE816_SIX);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1817_SEVEN:
				result.append( MCodeConstant.UNIE817_SEVEN);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1818_EIGHT:
				result.append( MCodeConstant.UNIE818_EIGHT);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1819_NINE:
				result.append( MCodeConstant.UNIE819_NINE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1820_A:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE821_A_ISOL_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE820_A_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1821_E:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE829_E_ISOL_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE828_E_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1822_I:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE831_I_ISOL_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nobreak_202f) {
						result.append( MCodeConstant.UNIE831_I_ISOL_VAR1);
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE830_I_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1823_O:
				result.append( MCodeConstant.UNIE838_O_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1824_U:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE842_U_INIT);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE841_U_ISOL_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nobreak_202f) {
						result.append( MCodeConstant.UNIE841_U_ISOL_VAR2);
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE840_U_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1825_OE:
				result.append( MCodeConstant.UNIE848_OE_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1826_UE:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE85F_UE_ISOL_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE852_UE_INIT);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIE851_UE_ISOL_VAR3);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nobreak_202f) {
						result.append( MCodeConstant.UNIE851_UE_ISOL_VAR3);
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE850_UE_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1827_EE:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE858_EE_ISOL);
						iResult++;
						break;
					}
				}
				result.append( MCodeConstant.UNIE858_EE_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1828_N:
				result.append( MCodeConstant.UNIE860_N_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1829_NG:
				result.append( MCodeConstant.UNIE868_NG_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182A_B:
				result.append( MCodeConstant.UNIE870_B_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182B_P:
				result.append( MCodeConstant.UNIE898_P_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182C_H:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIEA02_H_ISOL_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
							result.append( MCodeConstant.UNIEA00_H_ISOL_VAR2);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
					} else if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIE8C1_H_ISOL_VAR3);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}

				}
				result.append( MCodeConstant.UNIE8C0_H_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182D_G:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIEA1C_G_ISOL_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIEA1B_G_ISOL_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIE8E1_G_ISOL_VAR3);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE8E0_G_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182E_M:
				result.append( MCodeConstant.UNIE902_M_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182F_L:
				result.append( MCodeConstant.UNIE906_L_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1830_S:
				result.append( MCodeConstant.UNIE90A_S_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1831_SH:
				result.append( MCodeConstant.UNIE90E_SH_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1832_T:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE913_T_ISOL_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE912_T_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1833_D:
				result.append( MCodeConstant.UNIE919_D_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1834_CH:
				result.append( MCodeConstant.UNIE920_CH_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1835_J:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE925_J_ISOL_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE924_J_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1836_Y:
				result.append( MCodeConstant.UNIE929_Y_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1837_R:
				result.append( MCodeConstant.UNIE92E_R_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1838_W:
				result.append( MCodeConstant.UNIE932_W_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1839_F:
				result.append( MCodeConstant.UNIE938_F_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183A_K:
				result.append( MCodeConstant.UNIE960_K_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183B_KH:
				result.append( MCodeConstant.UNIE988_KH_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183C_TS:
				result.append( MCodeConstant.UNIE9B0_C_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183D_Z:
				result.append( MCodeConstant.UNIE9B4_Z_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183E_HH:
				result.append( MCodeConstant.UNIE9B8_HH_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183F_RH:
				result.append( MCodeConstant.UNIE9BC_RH_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1840_LH:
				result.append( MCodeConstant.UNIE9C0_LH_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1841_ZH:
				result.append( MCodeConstant.UNIE9C4_ZH_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1842_CHI:
				result.append( MCodeConstant.UNIE9C8_CHI_ISOL);
				CurrentStatus = ISOLATE;
				break;
				
			default :
				result.append( currentChar);
				CurrentStatus = ISOLATE;
				break;
		}
		
		nobreak_202f = false;		
    	return iResult;
    	
    }

    private int processInitial(StringBuffer docBuffer, int i, StringBuffer result ) {
    	
    	int iResult = i;
    	
		char currentChar = docBuffer.charAt(i);
		if( currentChar==MCodeConstant.UNI202F_NOBREAK){
			result.append( MCodeConstant.UNI202F_NOBREAK);
			nobreak_202f = true;
			CurrentStatus = ISOLATE;
			return iResult;
		}
		
		switch( currentChar ) {
			
			case  '!' :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == '?' ) {
						result.append( MCodeConstant.UNIE81B);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE81C);
				CurrentStatus = ISOLATE;
				break;
				
			case  '?' :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == '!' ) {
						result.append( MCodeConstant.UNIE81A);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE81D);
				CurrentStatus = ISOLATE;
				break;
				
			case  ';' :
				result.append( MCodeConstant.UNIE81E);
				CurrentStatus = ISOLATE;
				break;
				
			case  ',' :
				result.append( MCodeConstant.UNIE81F);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1800_BIRGA :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIEA24_BIRGA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIEA25_BIRGA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					} else if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIEA26_BIRGA_VAR3);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					} else if( nextChar == MCodeConstant.UNI180E_MVS ) {
						result.append( MCodeConstant.UNIEA27_BIRGA_VAR4);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					}
				}
				result.append( MCodeConstant.UNIE800_BIRGA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1801_ELLIPSIS:
				result.append( MCodeConstant.UNIE801_ELLIPSIS);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1802_COMMA:
				result.append( MCodeConstant.UNIE802_COMMA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1803_FULLSTOP:
				result.append( MCodeConstant.UNIE803_FULLSTOP);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1804_COLON:
				result.append( MCodeConstant.UNIE804_COLON);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1805_FOURDOT:
				result.append( MCodeConstant.UNIE805_FOURDOT);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1806:
				result.append( MCodeConstant.UNIE806);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1807:
				result.append( MCodeConstant.UNIE807);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1808:
				result.append( MCodeConstant.UNIE808);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1809:
				result.append( MCodeConstant.UNIE809);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180A_NIRUGU:
				result.append( MCodeConstant.UNIE80A_NIRUGU);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI180B_FVS1:
				result.append( MCodeConstant.UNIE80B);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180C_FVS2:
				result.append( MCodeConstant.UNIE80C);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180D_FVS3:
				result.append( MCodeConstant.UNIE80D);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180E_MVS:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI1820_A ) {
						result.append( MCodeConstant.UNIE827_A_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						masculine = true;
						break;
					}
					if( nextChar == MCodeConstant.UNI1821_E ) {
						result.append( MCodeConstant.UNIE82F_E_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						feminine = true;
						break;
					}
				} else {
					result.append( MCodeConstant.UNIE80E);
					CurrentStatus = ISOLATE;
				}
				break;
				
			case  MCodeConstant.UNI1810_ZERO:
				result.append( MCodeConstant.UNIE810_ZERO);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1811_ONE:
				result.append( MCodeConstant.UNI1811_ONE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1812_TWO:
				result.append( MCodeConstant.UNI1812_TWO);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1813_THREE:
				result.append( MCodeConstant.UNIE813_THREE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1814_FOUE:
				result.append( MCodeConstant.UNIE814_FOUR);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1815_FIVE:
				result.append( MCodeConstant.UNIE815_FIVE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1816_SIX:
				result.append( MCodeConstant.UNIE816_SIX);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1817_SEVEN:
				result.append( MCodeConstant.UNIE817_SEVEN);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1818_EIGHT:
				result.append( MCodeConstant.UNIE818_EIGHT);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1819_NINE:
				result.append( MCodeConstant.UNIE819_NINE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1820_A:
				masculine = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIEA21_A_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE822_A_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1821_E:
    			feminine = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE82B_E_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE82A_E_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1822_I:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE833_I_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nobreak_202f) {
						if( nextChar == MCodeConstant.UNI1822_I || nextChar == MCodeConstant.UNI1836_Y){
							if( !isFinal(docBuffer, i+1) ) {
								result.append( MCodeConstant.UNIE833_I_INIT_VAR1);
								result.append( MCodeConstant.UNIE834_I_MEDI);
								iResult++;
								CurrentStatus = INTERMEDIATE;
								break;
							} else {
								result.append( MCodeConstant.UNIE833_I_INIT_VAR1);
								result.append( MCodeConstant.UNIE837_I_FINA);
								iResult++;
								CurrentStatus = ISOLATE; 
								break;
							}
							
						} else {
							result.append( MCodeConstant.UNIE833_I_INIT_VAR1);
							result.append( MCodeConstant.UNIE834_I_MEDI);
							CurrentStatus = INTERMEDIATE;
							break;
						}
					}
				}
				result.append( MCodeConstant.UNIE832_I_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1823_O:
				masculine = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE83B_O_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nobreak_202f) {
						result.append( MCodeConstant.UNIE83B_O_INIT_VAR1);
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE83A_O_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1824_U:
				masculine = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE843_U_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nobreak_202f && nextChar == MCodeConstant.UNI1824_U ) {
						result.append( MCodeConstant.UNIE9CF_UE_ISOL_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nobreak_202f) {
						result.append( MCodeConstant.UNIE843_U_INIT_VAR1);
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE842_U_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1825_OE:
    			feminine = true;
				if( nobreak_202f) {
					result.append( MCodeConstant.UNIEA20_OE_MEDI_VAR1);
					CurrentStatus = INTERMEDIATE;
					break;
				}
				result.append( MCodeConstant.UNIE84A_OE_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1826_UE:
    			feminine = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nobreak_202f && nextChar == MCodeConstant.UNI1826_UE ) {
						result.append( MCodeConstant.UNIE9CF_UE_ISOL_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nobreak_202f) {
						result.append( MCodeConstant.UNIE85E_UE_INIT_VAR1);
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE852_UE_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1827_EE:
    			feminine = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE85B_EE_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE85A_EE_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1828_N:
				firstconsonant = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE863_N_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE862_N_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1829_NG:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI182C_H ) {
						if( i < docBuffer.length()-2){
							char thirdChar = docBuffer.charAt(i+2);
							if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
								result.append( MCodeConstant.UNIE86C_NGH_MEDI);
								iResult++;
								CurrentStatus = INTERMEDIATE;
								break;
							}
						}
					}
					if( nextChar == MCodeConstant.UNI182D_G ) {
						if( i < docBuffer.length()-2){
							char thirdChar = docBuffer.charAt(i+2);
							if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
								result.append( MCodeConstant.UNIE86D_NGG_MEDI);
								iResult++;
								CurrentStatus = INTERMEDIATE;
								break;
							}
						}
					}
					if( nextChar == MCodeConstant.UNI182E_M ) {
						result.append( MCodeConstant.UNIE86E_NGM_MEDI);
						iResult++;
						break;
					}
					if( nextChar == MCodeConstant.UNI182F_L ) {
						result.append( MCodeConstant.UNIE86F_NGL_MEDI);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					
				}
				result.append( MCodeConstant.UNIE869_NG_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI182A_B:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							result.append( MCodeConstant.UNIE874_BA_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE878_BE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE87C_BI_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							result.append( MCodeConstant.UNIE880_BO_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							result.append( MCodeConstant.UNIE884_BU_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							result.append( MCodeConstant.UNIE888_BOE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							result.append( MCodeConstant.UNIE88D_BUE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE892_BEE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							masculine = true;
							result.append( MCodeConstant.UNIE875_BA_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE879_BE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE87D_BI_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							masculine = true;
							result.append( MCodeConstant.UNIE881_BO_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							masculine = true;
							result.append( MCodeConstant.UNIE885_BU_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE889_BOE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
			    			if( i < docBuffer.length()-2 ){
				    			char thirdChar = docBuffer.charAt(i+2);
				    			if( thirdChar == MCodeConstant.UNI1826_UE && isFinal(docBuffer,i+2) ){
									result.append( MCodeConstant.UNIE9D0_BUE_ISOL_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;
				    			}
			    			}
							result.append( MCodeConstant.UNIE88E_BUE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE893_BEE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
					}
				}
				result.append( MCodeConstant.UNIE871_B_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI182B_P:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							result.append( MCodeConstant.UNIE89C_PA_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE8A0_PE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8A4_PI_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							result.append( MCodeConstant.UNIE8A8_PO_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							result.append( MCodeConstant.UNIE8AC_PU_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							result.append( MCodeConstant.UNIE8B0_POE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							result.append( MCodeConstant.UNIE8B5_PUE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE8BA_PEE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							masculine = true;
							result.append( MCodeConstant.UNIE89D_PA_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8A1_PE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8A5_PI_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							masculine = true;
							result.append( MCodeConstant.UNIE8A9_PO_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							masculine = true;
							result.append( MCodeConstant.UNIE8AD_PU_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8B1_POE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8B6_PUE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8BB_PEE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
					}
				}
				result.append( MCodeConstant.UNIE899_P_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI182C_H:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						if( i < docBuffer.length()-2) {
							char thirdChar = docBuffer.charAt(i+2);
							if( isFinal(docBuffer, i+2) ) {
								
								if( thirdChar == MCodeConstant.UNI1821_E ) {
									result.append( MCodeConstant.UNIEA05_HE_ISOL_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1822_I ) {
									result.append( MCodeConstant.UNIEA09_HI_ISOL_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1825_OE ) {
									result.append( MCodeConstant.UNIEA0D_HOE_ISOL_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1826_UE ) {
									result.append( MCodeConstant.UNIEA12_HUE_ISOL_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1827_EE ) {
									result.append( MCodeConstant.UNIEA17_HEE_ISOL_VAR1);
									iResult++;
									CurrentStatus = ISOLATE;
									break;
								}
							} else {
								if( thirdChar == MCodeConstant.UNI1821_E ) {
					    			feminine = true;
									result.append( MCodeConstant.UNIEA06_HE_INIT_VAR1);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1822_I ) {
									result.append( MCodeConstant.UNIEA0A_HI_INIT_VAR1);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1825_OE ) {
					    			feminine = true;
									result.append( MCodeConstant.UNIEA0E_HOE_INIT_VAR1);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1826_UE ) {
					    			feminine = true;
									result.append( MCodeConstant.UNIEA13_HUE_INIT_VAR1);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1827_EE ) {
					    			feminine = true;
									result.append( MCodeConstant.UNIEA18_HEE_INIT_VAR1);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
						result.append( MCodeConstant.UNIE8C3_H_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIEA03_H_INIT_VAR2);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIEA01_H_INIT_VAR3);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE8CA_HE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8CE_HI_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							result.append( MCodeConstant.UNIE8D2_HOE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							result.append( MCodeConstant.UNIE8D7_HUE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE8DC_HEE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8CB_HE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8CF_HI_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8D3_HOE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8D8_HUE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8DD_HEE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
					}
				}
				result.append( MCodeConstant.UNIE8C2_H_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI182D_G:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE8E3_G_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIEA1E_G_INIT_VAR2);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIEA1D_G_INIT_VAR3);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					} 
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE8EA_GE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8EE_GI_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							result.append( MCodeConstant.UNIE8F2_GOE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							result.append( MCodeConstant.UNIE8F7_GUE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE8FC_GEE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8EB_GE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8EF_GI_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8F3_GOE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8F8_GUE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8FD_GEE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
					}
				}
				result.append( MCodeConstant.UNIE8E2_G_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI182E_M:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE903_M_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI182F_L:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE907_L_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1830_S:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE90B_S_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1831_SH:
				firstconsonant = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE90B_S_INIT);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE90F_SH_INIT);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					} 
					if( nextChar == MCodeConstant.UNI1822_I ) {
						result.append( MCodeConstant.UNIE90B_S_INIT);
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE90F_SH_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1832_T:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE914_T_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1833_D:
				firstconsonant = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE91B_D_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI1827_EE ) {
						result.append( MCodeConstant.UNIE91B_D_INIT_VAR1);
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				if( nobreak_202f ) {
					result.append( MCodeConstant.UNIE91B_D_INIT_VAR1);
				} else {
					result.append( MCodeConstant.UNIE91A_D_INIT);
				}
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1834_CH:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE921_CH_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1835_J:
				firstconsonant = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nobreak_202f && nextChar == MCodeConstant.UNI180E_MVS ) {
						result.append( MCodeConstant.UNIE925_J_ISOL_VAR1);
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE926_J_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1836_Y:
				firstconsonant = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE92B_Y_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nobreak_202f && nextChar == MCodeConstant.UNI180E_MVS ) {
						result.append( MCodeConstant.UNIE92D_Y_FINA);
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nobreak_202f) {
						result.append( MCodeConstant.UNIE92B_Y_INIT_VAR1);
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE92A_Y_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1837_R:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE92F_R_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1838_W:
				firstconsonant = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE934_W_INIT_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE933_W_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1839_F:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							result.append( MCodeConstant.UNIE93C_FA_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE940_FE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE944_FI_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							result.append( MCodeConstant.UNIE948_FO_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							result.append( MCodeConstant.UNIE94C_FU_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							result.append( MCodeConstant.UNIE950_FOE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							result.append( MCodeConstant.UNIE955_FUE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE95A_FEE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							masculine = true;
							result.append( MCodeConstant.UNIE93D_FA_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE941_FE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE945_FI_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							masculine = true;
							result.append( MCodeConstant.UNIE949_FO_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							masculine = true;
							result.append( MCodeConstant.UNIE94D_FU_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE951_FOE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE956_FUE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE95B_FEE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
					}
				}
				result.append( MCodeConstant.UNIE939_F_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183A_K:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							result.append( MCodeConstant.UNIE964_KA_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE968_KE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE96C_KI_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							result.append( MCodeConstant.UNIE970_KO_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							result.append( MCodeConstant.UNIE974_KU_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							result.append( MCodeConstant.UNIE978_KOE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							result.append( MCodeConstant.UNIE97D_KUE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE982_KEE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							masculine = true;
							result.append( MCodeConstant.UNIE965_KA_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE969_KE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE96D_KI_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							masculine = true;
							result.append( MCodeConstant.UNIE971_KO_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							masculine = true;
							result.append( MCodeConstant.UNIE975_KU_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE979_KOE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE97E_KUE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE983_KEE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
					}
				}
				result.append( MCodeConstant.UNIE961_K_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183B_KH:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							result.append( MCodeConstant.UNIE98C_KHA_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE990_KHE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE994_KHI_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							result.append( MCodeConstant.UNIE998_KHO_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							result.append( MCodeConstant.UNIE99C_KHU_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							result.append( MCodeConstant.UNIE9A0_KHOE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							result.append( MCodeConstant.UNIE9A5_KHUE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE9AA_KHEE_ISOL);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							masculine = true;
							result.append( MCodeConstant.UNIE98D_KHA_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE991_KHE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE995_KHI_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							masculine = true;
							result.append( MCodeConstant.UNIE999_KHO_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							masculine = true;
							result.append( MCodeConstant.UNIE99D_KHU_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE9A1_KHOE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE9A6_KHUE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE9AB_KHEE_INIT);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
					}
				}
				result.append( MCodeConstant.UNIE989_KH_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183C_TS:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE9B1_C_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183D_Z:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE9B5_Z_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183E_HH:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE9B9_HH_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183F_RH:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE9BD_RH_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1840_LH:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE9C1_LH_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1841_ZH:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE9C5_ZH_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1842_CHI:
				firstconsonant = true;
				result.append( MCodeConstant.UNIE9C9_CHI_INIT);
				CurrentStatus = INTERMEDIATE;
				break;
				
			default :
				result.append( currentChar );
				CurrentStatus = ISOLATE;
				break;
		}
		
		nobreak_202f = false;		
    	return iResult;    	
    }

    private int processIntermediate(StringBuffer docBuffer, int i, StringBuffer result ) {
    	
    	int iResult = i;
    	
		char currentChar = docBuffer.charAt(i);
		if( currentChar==MCodeConstant.UNI202F_NOBREAK){
			result.append( MCodeConstant.UNI202F_NOBREAK);
			nobreak_202f = true;
			CurrentStatus = ISOLATE;
			return iResult;
		}
		
		switch( currentChar ) {
			
			case  '!' :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == '?' ) {
						result.append( MCodeConstant.UNIE81B);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE81C);
				CurrentStatus = ISOLATE;
				break;
				
			case  '?' :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == '!' ) {
						result.append( MCodeConstant.UNIE81A);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE81D);
				CurrentStatus = ISOLATE;
				break;
				
			case  ';' :
				result.append( MCodeConstant.UNIE81E);
				CurrentStatus = ISOLATE;
				break;
				
			case  ',' :
				result.append( MCodeConstant.UNIE81F);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1800_BIRGA :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIEA24_BIRGA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIEA25_BIRGA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					} else if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIEA26_BIRGA_VAR3);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					} else if( nextChar == MCodeConstant.UNI180E_MVS ) {
						result.append( MCodeConstant.UNIEA27_BIRGA_VAR4);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					}
				}
				result.append( MCodeConstant.UNIE800_BIRGA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1801_ELLIPSIS:
				result.append( MCodeConstant.UNIE801_ELLIPSIS);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1802_COMMA:
				result.append( MCodeConstant.UNIE802_COMMA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1803_FULLSTOP:
				result.append( MCodeConstant.UNIE803_FULLSTOP);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1804_COLON:
				result.append( MCodeConstant.UNIE804_COLON);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1805_FOURDOT:
				result.append( MCodeConstant.UNIE805_FOURDOT);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1806:
				result.append( MCodeConstant.UNIE806);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1807:
				result.append( MCodeConstant.UNIE807);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1808:
				result.append( MCodeConstant.UNIE808);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1809:
				result.append( MCodeConstant.UNIE809);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180A_NIRUGU:
				result.append( MCodeConstant.UNIE80A_NIRUGU);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI180B_FVS1:
				result.append( MCodeConstant.UNIE80B);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180C_FVS2:
				result.append( MCodeConstant.UNIE80C);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180D_FVS3:
				result.append( MCodeConstant.UNIE80D);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180E_MVS:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI1820_A ) {
						masculine = true;
						result.append( MCodeConstant.UNIE827_A_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI1821_E ) {
						feminine = true;
						result.append( MCodeConstant.UNIE82F_E_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				} else {
					result.append( MCodeConstant.UNIE80E);
					CurrentStatus = ISOLATE;
				}
				break;
				
			case  MCodeConstant.UNI1810_ZERO:
				result.append( MCodeConstant.UNIE810_ZERO);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1811_ONE:
				result.append( MCodeConstant.UNI1811_ONE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1812_TWO:
				result.append( MCodeConstant.UNI1812_TWO);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1813_THREE:
				result.append( MCodeConstant.UNIE813_THREE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1814_FOUE:
				result.append( MCodeConstant.UNIE814_FOUR);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1815_FIVE:
				result.append( MCodeConstant.UNIE815_FIVE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1816_SIX:
				result.append( MCodeConstant.UNIE816_SIX);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1817_SEVEN:
				result.append( MCodeConstant.UNIE817_SEVEN);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1818_EIGHT:
				result.append( MCodeConstant.UNIE818_EIGHT);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1819_NINE:
				result.append( MCodeConstant.UNIE819_NINE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1820_A:
				masculine = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE824_A_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE823_A_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1821_E:
    			feminine = true;
				result.append( MCodeConstant.UNIE82C_E_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1822_I:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE835_I_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE836_I_MEDI_VAR2);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIE834_I_MEDI);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE834_I_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1823_O:
				masculine = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE83D_O_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE83C_O_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1824_U:
				masculine = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE845_U_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE844_U_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1825_OE:
    			feminine = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE84C_OE_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE84D_OE_MEDI_VAR2);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				if( firstconsonant ) {
					result.append( MCodeConstant.UNIE84C_OE_MEDI_VAR1);
					CurrentStatus = INTERMEDIATE;
					break;
				}
				result.append( MCodeConstant.UNIE84B_OE_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1826_UE:
    			feminine = true;
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE854_UE_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE855_UE_MEDI_VAR2);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIE853_UE_MEDI);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				if( firstconsonant ) {
					result.append( MCodeConstant.UNIE854_UE_MEDI_VAR1);
					CurrentStatus = INTERMEDIATE;
					break;
				}
				result.append( MCodeConstant.UNIE853_UE_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1827_EE:
    			feminine = true;
				result.append( MCodeConstant.UNIE85C_EE_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1828_N:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE865_N_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIE865_N_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI1820_A || nextChar == MCodeConstant.UNI1821_E || 
							nextChar == MCodeConstant.UNI1822_I || nextChar == MCodeConstant.UNI1823_O || 
							nextChar == MCodeConstant.UNI1824_U || nextChar == MCodeConstant.UNI1825_OE || 
							nextChar == MCodeConstant.UNI1826_UE || nextChar == MCodeConstant.UNI1827_EE ) {
						result.append( MCodeConstant.UNIE865_N_MEDI_VAR1);
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180E_MVS || nextChar == MCodeConstant.UNI202F_NOBREAK) {
						result.append( MCodeConstant.UNIE867_N_FINA_VAR1);
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE864_N_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1829_NG:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI182C_H ) {
						if( i < docBuffer.length()-2){
							char thirdChar = docBuffer.charAt(i+2);
							if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
								result.append( MCodeConstant.UNIE86C_NGH_MEDI);
								iResult++;
								CurrentStatus = INTERMEDIATE;
								break;
							}
							if( thirdChar==MCodeConstant.UNI180E_MVS ){
								result.append( MCodeConstant.UNIE9E4_NGH_FINA);
								iResult++;
								CurrentStatus = INTERMEDIATE;
								break;
							}
						}
					}
					if( nextChar == MCodeConstant.UNI182D_G ) {
						if( i < docBuffer.length()-2){
							char thirdChar = docBuffer.charAt(i+2);
							if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
								result.append( MCodeConstant.UNIE86D_NGG_MEDI);
								iResult++;
								CurrentStatus = INTERMEDIATE;
								break;
							}
							if( thirdChar==MCodeConstant.UNI180E_MVS ){
								result.append( MCodeConstant.UNIE9E5_NGG_FINA);
								iResult++;
								CurrentStatus = INTERMEDIATE;
								break;
							}
						}
					}
					if( nextChar == MCodeConstant.UNI1828_N ) {
						if( i < docBuffer.length()-2){
							char thirdChar = docBuffer.charAt(i+2);
							if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U
									|| thirdChar==MCodeConstant.UNI1822_I || thirdChar==MCodeConstant.UNI1825_OE || thirdChar==MCodeConstant.UNI1826_UE
									|| thirdChar==MCodeConstant.UNI1827_EE ){
								result.append( MCodeConstant.UNIE9E3_NGN_MEDI);
								iResult++;
								CurrentStatus = INTERMEDIATE;
								break;
							}
						}
					}
					if( nextChar == MCodeConstant.UNI182E_M ) {
						result.append( MCodeConstant.UNIE86E_NGM_MEDI);
						iResult++;
						break;
					}
					if( nextChar == MCodeConstant.UNI182F_L ) {
						result.append( MCodeConstant.UNIE86F_NGL_MEDI);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE86A_NG_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI182A_B:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							result.append( MCodeConstant.UNIE877_BA_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE87B_BE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE87F_BI_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							result.append( MCodeConstant.UNIE883_BO_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							result.append( MCodeConstant.UNIE887_BU_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE88C_BOE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}
							}
							result.append( MCodeConstant.UNIE88B_BOE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE891_BUE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE890_BUE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE895_BEE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							masculine = true;
							result.append( MCodeConstant.UNIE876_BA_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE87A_BE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE87E_BI_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							masculine = true;
							result.append( MCodeConstant.UNIE882_BO_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							masculine = true;
							result.append( MCodeConstant.UNIE886_BU_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9D1_BOE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE88A_BOE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9D2_BUE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE88F_BUE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE894_BEE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1828_N ) {
							result.append( MCodeConstant.UNIE9F0_BN_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182E_M ) {
							result.append( MCodeConstant.UNIE896_BM_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182F_L ) {
							result.append( MCodeConstant.UNIE897_BL_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182C_H ) {
							if( i < docBuffer.length()-2){
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
									result.append( MCodeConstant.UNIE9E6_BH_MEDI);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
						if( nextChar == MCodeConstant.UNI182D_G ) {
							if( i < docBuffer.length()-2){
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
									result.append( MCodeConstant.UNIE9E7_BG_MEDI);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
					}
				}
				result.append( MCodeConstant.UNIE872_B_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI182B_P:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							result.append( MCodeConstant.UNIE89F_PA_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE8A3_PE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8A7_PI_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							result.append( MCodeConstant.UNIE8AB_PO_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							result.append( MCodeConstant.UNIE8AF_PU_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE8B4_POE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE8B3_POE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE8B9_PUE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE8B8_PUE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE8BD_PEE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							masculine = true;
							result.append( MCodeConstant.UNIE89E_PA_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8A2_PE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8A6_PI_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							masculine = true;
							result.append( MCodeConstant.UNIE8AB_PO_FINA);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							masculine = true;
							result.append( MCodeConstant.UNIE8AF_PU_FINA);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9D3_POE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}
							} 
							result.append( MCodeConstant.UNIE8B2_POE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9D4_PUE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE8B7_PUE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8BC_PEE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1828_N ) {
							result.append( MCodeConstant.UNIE9F1_PN_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182E_M ) {
							result.append( MCodeConstant.UNIE8BE_PM_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182F_L ) {
							result.append( MCodeConstant.UNIE8BF_PL_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182C_H ) {
							if( i < docBuffer.length()-2){
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
									result.append( MCodeConstant.UNIE9E8_PH_MEDI);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
						if( nextChar == MCodeConstant.UNI182D_G ) {
							if( i < docBuffer.length()-2){
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
									result.append( MCodeConstant.UNIE9E9_PG_MEDI);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
					}
				}
				result.append( MCodeConstant.UNIE89A_P_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI182C_H:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						if(  i < docBuffer.length()-2 ) {
							char thirdChar = docBuffer.charAt(i+2);						
							if( isFinal(docBuffer, i+2) ) {
								if( thirdChar == MCodeConstant.UNI1821_E ) {
									result.append( MCodeConstant.UNIEA08_HE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1822_I ) {
									result.append( MCodeConstant.UNIEA0C_HI_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1825_OE ) {
									if( i < docBuffer.length()-3 ) {
										char forthChar = docBuffer.charAt(i+3);
										if( forthChar==MCodeConstant.UNI180B_FVS1 ){
											result.append( MCodeConstant.UNIEA11_HOE_FINA_VAR3);
											iResult++;
											iResult++;
											iResult++;
											CurrentStatus = ISOLATE;
											break;									
										}
									}
									result.append( MCodeConstant.UNIEA10_HOE_FINA_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1826_UE ) {
									if( i < docBuffer.length()-3 ) {
										char forthChar = docBuffer.charAt(i+3);
										if( forthChar==MCodeConstant.UNI180B_FVS1 ){
											result.append( MCodeConstant.UNIEA16_HUE_FINA_VAR3);
											iResult++;
											iResult++;
											iResult++;
											CurrentStatus = ISOLATE;
											break;									
										}
									}
									result.append( MCodeConstant.UNIEA15_HUE_FINA_VAR2);
									iResult++;
									CurrentStatus = ISOLATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1827_EE ) {
									result.append( MCodeConstant.UNIEA1A_HEE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;
								}
							} else {
								if( thirdChar == MCodeConstant.UNI1821_E ) {
					    			feminine = true;
									result.append( MCodeConstant.UNIEA07_HE_MEDI_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1822_I ) {
									result.append( MCodeConstant.UNIEA0B_HI_MEDI_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1825_OE ) {
					    			feminine = true;
									result.append( MCodeConstant.UNIEA0F_HOE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1826_UE ) {
					    			feminine = true;
									result.append( MCodeConstant.UNIEA14_HUE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
								if( thirdChar == MCodeConstant.UNI1827_EE ) {
					    			feminine = true;
									result.append( MCodeConstant.UNIEA19_HEE_MEDI_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
						result.append( MCodeConstant.UNIE8C5_H_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIE8C6_H_MEDI_VAR3);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIEA04_H_MEDI_VAR2);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180E_MVS || nextChar == MCodeConstant.UNI202F_NOBREAK) {
						result.append( MCodeConstant.UNIE8C7_H_FINA);
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE8CD_HE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8D1_HI_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE8D6_HOE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}
							}
							result.append( MCodeConstant.UNIE8D5_HOE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE8DB_HUE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;
								}
							}
							result.append( MCodeConstant.UNIE8DA_HUE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE8DF_HEE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8CC_HE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8D0_HI_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9D5_HOE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
							result.append( MCodeConstant.UNIE8D4_HOE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9D6_HUE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}
								result.append( MCodeConstant.UNIE8D9_HUE_MEDI);
								iResult++;
								CurrentStatus = INTERMEDIATE;
								break;
							}
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8DE_HEE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
					}
				}
				result.append( MCodeConstant.UNIE8C4_H_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;

			case  MCodeConstant.UNI182D_G:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE8E5_G_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}  
					if( nextChar == MCodeConstant.UNI1820_A || 
							nextChar == MCodeConstant.UNI1823_O || 
							nextChar == MCodeConstant.UNI1824_U ) {
						char prevChar = docBuffer.charAt(i-1);
						if( prevChar != MCodeConstant.UNI1830_S && prevChar != MCodeConstant.UNI1833_D && prevChar != MCodeConstant.UNI1832_T){
							result.append( MCodeConstant.UNIE8E5_G_MEDI_VAR1);
							CurrentStatus = INTERMEDIATE;
							break;
						}
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ){
						result.append( MCodeConstant.UNIE8E6_G_MEDI_VAR2);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180D_FVS3 ){
						result.append( MCodeConstant.UNIEA1F_G_MEDI_VAR3);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( feminine && nextChar!=MCodeConstant.UNI1821_E && nextChar!=MCodeConstant.UNI1825_OE
							&& nextChar!=MCodeConstant.UNI1822_I
							&& nextChar!=MCodeConstant.UNI1826_UE && nextChar!=MCodeConstant.UNI1827_EE ) {
						result.append( MCodeConstant.UNIEA1F_G_MEDI_VAR3);
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180E_MVS || nextChar == MCodeConstant.UNI202F_NOBREAK) {
						result.append( MCodeConstant.UNIE8E8_G_FINA_VAR1);
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE8ED_GE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8F1_GI_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE8F6_GOE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}
								result.append( MCodeConstant.UNIE8F5_GOE_FINA);
								iResult++;
								CurrentStatus = ISOLATE;
								break;
							}
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE8FB_GUE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}
								result.append( MCodeConstant.UNIE8FA_GUE_FINA);
								iResult++;
								CurrentStatus = ISOLATE;
								break;
							}
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE8FF_GEE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8EC_GE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE8F0_GI_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							if( i < docBuffer.length()-1 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9D7_GOE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
								result.append( MCodeConstant.UNIE8F4_GOE_MEDI);
								iResult++;
								CurrentStatus = INTERMEDIATE;
								break;
							}
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							if( i < docBuffer.length()-1 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9D8_GUE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}
								result.append( MCodeConstant.UNIE8F9_GUE_MEDI);
								iResult++;
								CurrentStatus = INTERMEDIATE;
								break;
							}
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE8FE_GEE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( feminine && nextChar == MCodeConstant.UNI1828_N ) {
							result.append( MCodeConstant.UNIE9F2_GN_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( feminine && nextChar == MCodeConstant.UNI182E_M ) {
							result.append( MCodeConstant.UNIE900_GM_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( feminine && nextChar == MCodeConstant.UNI182F_L ) {
							result.append( MCodeConstant.UNIE901_GL_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
					}
				}
				if( feminine==true || feminine == false && masculine==false )
					result.append( MCodeConstant.UNIEA1F_G_MEDI_VAR3);
				else
					result.append( MCodeConstant.UNIE8E4_G_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI182E_M:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180E_MVS || nextChar == MCodeConstant.UNI202F_NOBREAK) {
						result.append( MCodeConstant.UNIE905_M_FINA);
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI182E_M ) {
						result.append( MCodeConstant.UNIE9E0_MM_MEDI);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI182F_L ) {
						result.append( MCodeConstant.UNIE9E1_ML_MEDI);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE904_M_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI182F_L:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180E_MVS || nextChar == MCodeConstant.UNI202F_NOBREAK) {
						result.append( MCodeConstant.UNIE909_L_FINA);
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI182F_L ) {
						result.append( MCodeConstant.UNIE9E2_LL_MEDI);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE908_L_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1830_S:
				result.append( MCodeConstant.UNIE90C_S_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1831_SH:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI1822_I ) {
						result.append( MCodeConstant.UNIE90C_S_MEDI);
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar==MCodeConstant.UNI180B_FVS1 ){
						result.append( MCodeConstant.UNIE90C_S_MEDI);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;									
					}
					if( nextChar==MCodeConstant.UNI180C_FVS2 ){
						result.append( MCodeConstant.UNIE910_SH_MEDI);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;									
					}
				}
				result.append( MCodeConstant.UNIE910_SH_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1832_T:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE916_T_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE915_T_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1833_D:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE91D_D_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE91C_D_MEDI);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI1820_A || nextChar == MCodeConstant.UNI1821_E || 
							nextChar == MCodeConstant.UNI1822_I || nextChar == MCodeConstant.UNI1823_O || 
							nextChar == MCodeConstant.UNI1824_U || nextChar == MCodeConstant.UNI1825_OE || 
							nextChar == MCodeConstant.UNI1826_UE || nextChar == MCodeConstant.UNI1827_EE ) {
						
						result.append( MCodeConstant.UNIE91D_D_MEDI_VAR1);
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE91C_D_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1834_CH:
				result.append( MCodeConstant.UNIE922_CH_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1835_J:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE925_J_ISOL_VAR1);   // TODO
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIEA22_Y_MEDI_VAR1);  // TODO
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE927_J_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1836_Y:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180E_MVS || nextChar == MCodeConstant.UNI202F_NOBREAK) {
						result.append( MCodeConstant.UNIE92D_Y_FINA);
						CurrentStatus = INTERMEDIATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIEA22_Y_MEDI_VAR1);
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE92C_Y_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1837_R:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180E_MVS || nextChar == MCodeConstant.UNI202F_NOBREAK) {
						result.append( MCodeConstant.UNIE931_R_FINA);
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE930_R_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1838_W:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE83C_O_MEDI);   // TODO
						iResult++;
						CurrentStatus = INTERMEDIATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180E_MVS || nextChar == MCodeConstant.UNI202F_NOBREAK) {
						result.append( MCodeConstant.UNIE937_W_FINA_VAR1);
						CurrentStatus = INTERMEDIATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE935_W_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1839_F:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							result.append( MCodeConstant.UNIE93F_FA_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE943_FE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE947_FI_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							result.append( MCodeConstant.UNIE94B_FO_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							result.append( MCodeConstant.UNIE94F_FU_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE954_FOE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}
							}
							result.append( MCodeConstant.UNIE953_FOE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE959_FUE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}
							}
							result.append( MCodeConstant.UNIE958_FUE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE95D_FEE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							masculine = true;
							result.append( MCodeConstant.UNIE93E_FA_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE942_FE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE946_FI_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							masculine = true;
							result.append( MCodeConstant.UNIE94A_FO_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							masculine = true;
							result.append( MCodeConstant.UNIE94E_FU_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9D9_FOE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}
							}
							result.append( MCodeConstant.UNIE952_FOE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9DA_FUE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE957_FUE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE95C_FEE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1828_N ) {
							result.append( MCodeConstant.UNIE9F3_FN_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182E_M ) {
							result.append( MCodeConstant.UNIE95E_FM_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182F_L ) {
							result.append( MCodeConstant.UNIE95F_FL_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182C_H ) {
							if( i < docBuffer.length()-2 ){
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
									result.append( MCodeConstant.UNIE9EA_FH_MEDI);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
						if( nextChar == MCodeConstant.UNI182D_G ) {
							if( i < docBuffer.length()-2 ){
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
									result.append( MCodeConstant.UNIE9EB_FG_MEDI);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
					}
				}
				result.append( MCodeConstant.UNIE93A_F_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183A_K:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							result.append( MCodeConstant.UNIE967_KA_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE96B_KE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE96F_KI_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							result.append( MCodeConstant.UNIE973_KO_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							result.append( MCodeConstant.UNIE977_KU_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE97C_KOE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}
							}
							result.append( MCodeConstant.UNIE97B_KOE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE981_KUE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE980_KUE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE985_KEE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							masculine = true;
							result.append( MCodeConstant.UNIE966_KA_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE96A_KE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE96E_KI_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							masculine = true;
							result.append( MCodeConstant.UNIE972_KO_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							masculine = true;
							result.append( MCodeConstant.UNIE976_KU_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9DB_KOE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}
							}
							result.append( MCodeConstant.UNIE97A_KOE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9DC_KUE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE97F_KUE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE984_KEE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1828_N ) {
							result.append( MCodeConstant.UNIE9F4_KN_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182E_M ) {
							result.append( MCodeConstant.UNIE986_KM_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182F_L ) {
							result.append( MCodeConstant.UNIE987_KL_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182C_H ) {
							if( i < docBuffer.length()-2){
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
									result.append( MCodeConstant.UNIE9EC_KH_MEDI);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
						if( nextChar == MCodeConstant.UNI182D_G ) {
							if( i < docBuffer.length()-2){
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
									result.append( MCodeConstant.UNIE9ED_KG_MEDI);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
					}
				}
				result.append( MCodeConstant.UNIE962_K_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183B_KH:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( isFinal(docBuffer, i+1) ) {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							result.append( MCodeConstant.UNIE98F_KHA_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
							result.append( MCodeConstant.UNIE993_KHE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE997_KHI_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							result.append( MCodeConstant.UNIE99B_KHO_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							result.append( MCodeConstant.UNIE99F_KHU_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE9A4_KHOE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}
							}
							result.append( MCodeConstant.UNIE9A3_KHOE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180B_FVS1 ){
									result.append( MCodeConstant.UNIE9A9_KHUE_FINA_VAR1);
									iResult++;
									iResult++;
									CurrentStatus = ISOLATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE9A8_KHUE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
							result.append( MCodeConstant.UNIE895_BEE_FINA);
							iResult++;
							CurrentStatus = ISOLATE;
							break;
						}
					} else {
						if( nextChar == MCodeConstant.UNI1820_A ) {
							masculine = true;
							result.append( MCodeConstant.UNIE98E_KHA_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1821_E ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE992_KHE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1822_I ) {
							result.append( MCodeConstant.UNIE996_KHI_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1823_O ) {
							masculine = true;
							result.append( MCodeConstant.UNIE99A_KHO_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1824_U ) {
							masculine = true;
							result.append( MCodeConstant.UNIE99E_KHU_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1825_OE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9DD_KHOE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE9A2_KHOE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1826_UE ) {
			    			feminine = true;
							if( i < docBuffer.length()-2 ) {
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI180C_FVS2 ){
									result.append( MCodeConstant.UNIE9DE_KHUE_MEDI_VAR2);
									iResult++;
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;									
								}								
							}
							result.append( MCodeConstant.UNIE9A7_KHUE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1827_EE ) {
			    			feminine = true;
							result.append( MCodeConstant.UNIE9AC_KHEE_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI1828_N ) {
							result.append( MCodeConstant.UNIE9F5_KHN_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182E_M ) {
							result.append( MCodeConstant.UNIE9AE_KHM_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182F_L ) {
							result.append( MCodeConstant.UNIE9AF_KHL_MEDI);
							iResult++;
							CurrentStatus = INTERMEDIATE;
							break;
						}
						if( nextChar == MCodeConstant.UNI182C_H ) {
							if( i < docBuffer.length()-2){
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
									result.append( MCodeConstant.UNIE9EE_KHH_MEDI);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
						if( nextChar == MCodeConstant.UNI182D_G ) {
							if( i < docBuffer.length()-2){
								char thirdChar = docBuffer.charAt(i+2);
								if( thirdChar==MCodeConstant.UNI1820_A || thirdChar==MCodeConstant.UNI1823_O || thirdChar==MCodeConstant.UNI1824_U ){
									result.append( MCodeConstant.UNIE9EF_KHG_MEDI);
									iResult++;
									CurrentStatus = INTERMEDIATE;
									break;
								}
							}
						}
					}
				}
				result.append( MCodeConstant.UNIE98A_KH_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183C_TS:
				result.append( MCodeConstant.UNIE9B2_C_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183D_Z:
				result.append( MCodeConstant.UNIE9B6_Z_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183E_HH:
				result.append( MCodeConstant.UNIE9BA_HH_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI183F_RH:
				result.append( MCodeConstant.UNIE9BE_RH_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1840_LH:
				result.append( MCodeConstant.UNIE9C2_LH_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1841_ZH:
				result.append( MCodeConstant.UNIE9C6_ZH_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			case  MCodeConstant.UNI1842_CHI:
				result.append( MCodeConstant.UNIE9CA_CHI_MEDI);
				CurrentStatus = INTERMEDIATE;
				break;
				
			default :
				result.append( currentChar );
				CurrentStatus = ISOLATE;
				break;
		}
		
		nobreak_202f = false;		
		firstconsonant = false;
    	return iResult;    	
    }

    private int processFinal(StringBuffer docBuffer, int i, StringBuffer result ) {
    	
    	int iResult = i;
    	
		char currentChar = docBuffer.charAt(i);
		if( currentChar==MCodeConstant.UNI202F_NOBREAK){
			result.append( MCodeConstant.UNI202F_NOBREAK);
			nobreak_202f = true;
			CurrentStatus = ISOLATE;
			return iResult;
		}
		
		switch( currentChar ) {
			
			case  '!' :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == '?' ) {
						result.append( MCodeConstant.UNIE81B);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE81C);
				CurrentStatus = ISOLATE;
				break;
				
			case  '?' :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == '!' ) {
						result.append( MCodeConstant.UNIE81A);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE81D);
				CurrentStatus = ISOLATE;
				break;
				
			case  ';' :
				result.append( MCodeConstant.UNIE81E);
				CurrentStatus = ISOLATE;
				break;
				
			case  ',' :
				result.append( MCodeConstant.UNIE81F);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1800_BIRGA :
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIEA24_BIRGA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					} else if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIEA25_BIRGA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					} else if( nextChar == MCodeConstant.UNI180D_FVS3 ) {
						result.append( MCodeConstant.UNIEA26_BIRGA_VAR3);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					} else if( nextChar == MCodeConstant.UNI180E_MVS ) {
						result.append( MCodeConstant.UNIEA27_BIRGA_VAR4);
						iResult++;
						CurrentStatus = ISOLATE;
						break;						
					}
				}
				result.append( MCodeConstant.UNIE800_BIRGA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1801_ELLIPSIS:
				result.append( MCodeConstant.UNIE801_ELLIPSIS);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1802_COMMA:
				result.append( MCodeConstant.UNIE802_COMMA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1803_FULLSTOP:
				result.append( MCodeConstant.UNIE803_FULLSTOP);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1804_COLON:
				result.append( MCodeConstant.UNIE804_COLON);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1805_FOURDOT:
				result.append( MCodeConstant.UNIE805_FOURDOT);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1806:
				result.append( MCodeConstant.UNIE806);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1807:
				result.append( MCodeConstant.UNIE807);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1808:
				result.append( MCodeConstant.UNIE808);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1809:
				result.append( MCodeConstant.UNIE809);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180A_NIRUGU:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE80F_SMALLTAIL);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE80A_NIRUGU);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180B_FVS1:
				result.append( MCodeConstant.UNIE80B);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180C_FVS2:
				result.append( MCodeConstant.UNIE80C);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180D_FVS3:
				result.append( MCodeConstant.UNIE80D);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI180E_MVS:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI1820_A ) {
						result.append( MCodeConstant.UNIE827_A_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI1821_E ) {
						result.append( MCodeConstant.UNIE82F_E_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				} else {
					result.append( MCodeConstant.UNIE80E);
					CurrentStatus = ISOLATE;
				}
				break;
				
			case  MCodeConstant.UNI1810_ZERO:
				result.append( MCodeConstant.UNIE810_ZERO);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1811_ONE:
				result.append( MCodeConstant.UNI1811_ONE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1812_TWO:
				result.append( MCodeConstant.UNI1812_TWO);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1813_THREE:
				result.append( MCodeConstant.UNIE813_THREE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1814_FOUE:
				result.append( MCodeConstant.UNIE814_FOUR);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1815_FIVE:
				result.append( MCodeConstant.UNIE815_FIVE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1816_SIX:
				result.append( MCodeConstant.UNIE816_SIX);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1817_SEVEN:
				result.append( MCodeConstant.UNIE817_SEVEN);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1818_EIGHT:
				result.append( MCodeConstant.UNIE818_EIGHT);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1819_NINE:
				result.append( MCodeConstant.UNIE819_NINE);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1820_A:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE826_A_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE827_A_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE825_A_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1821_E:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE82E_E_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE82F_E_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE82D_E_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1822_I:
				result.append( MCodeConstant.UNIE837_I_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1823_O:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE83F_O_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE83E_O_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1824_U:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE847_U_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE846_U_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1825_OE:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE84F_OE_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE84E_OE_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1826_UE:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE857_UE_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE856_UE_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1827_EE:
				result.append( MCodeConstant.UNIE85D_EE_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1828_N:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE867_N_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					} else
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIEA23_N_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE866_N_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1829_NG:
				result.append( MCodeConstant.UNIE86B_NG_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182A_B:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE9CE_B_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE873_B_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182B_P:
				result.append( MCodeConstant.UNIE89B_P_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182C_H:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE8C8_H_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE8C9_H_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE8C7_H_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182D_G:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE8E8_G_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE8E9_G_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( feminine ) {
						result.append( MCodeConstant.UNIE8E9_G_FINA_VAR2);
						CurrentStatus = ISOLATE;
						break;
					}
				}
				if( feminine==true || feminine == false && masculine==false )
					result.append( MCodeConstant.UNIE8E9_G_FINA_VAR2);
				else
					result.append( MCodeConstant.UNIE8E7_G_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182E_M:
				result.append( MCodeConstant.UNIE905_M_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI182F_L:
				result.append( MCodeConstant.UNIE909_L_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1830_S:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE9CC_S_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
					if( nextChar == MCodeConstant.UNI180C_FVS2 ) {
						result.append( MCodeConstant.UNIE9CD_S_FINA_VAR2);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE90D_S_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1831_SH:
				result.append( MCodeConstant.UNIE911_SH_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1832_T:
				result.append( MCodeConstant.UNIE918_T_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1833_D:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE91F_D_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE91E_D_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1834_CH:
				result.append( MCodeConstant.UNIE923_CH_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1835_J:
				result.append( MCodeConstant.UNIE928_J_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1836_Y:
				result.append( MCodeConstant.UNIE92D_Y_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1837_R:
				result.append( MCodeConstant.UNIE931_R_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1838_W:
				if( i < docBuffer.length()-1 ) {
					char nextChar = docBuffer.charAt(i+1);
					if( nextChar == MCodeConstant.UNI180B_FVS1 ) {
						result.append( MCodeConstant.UNIE937_W_FINA_VAR1);
						iResult++;
						CurrentStatus = ISOLATE;
						break;
					}
				}
				result.append( MCodeConstant.UNIE936_W_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1839_F:
				result.append( MCodeConstant.UNIE93B_F_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183A_K:
				result.append( MCodeConstant.UNIE963_K_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183B_KH:
				result.append( MCodeConstant.UNIE98B_KH_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183C_TS:
				result.append( MCodeConstant.UNIE9B3_C_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183D_Z:
				result.append( MCodeConstant.UNIE9B7_Z_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183E_HH:
				result.append( MCodeConstant.UNIE9BB_HH_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI183F_RH:
				result.append( MCodeConstant.UNIE9BF_RH_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1840_LH:
				result.append( MCodeConstant.UNIE9C3_LH_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1841_ZH:
				result.append( MCodeConstant.UNIE9C7_ZH_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			case  MCodeConstant.UNI1842_CHI:
				result.append( MCodeConstant.UNIE9CB_CHI_FINA);
				CurrentStatus = ISOLATE;
				break;
				
			default :
				result.append( currentChar );
				CurrentStatus = ISOLATE;
				break;
		}
		
		nobreak_202f = false;		
    	return iResult;
    }

    private static boolean isFinal ( StringBuffer docBuffer, int i ) {

    	boolean result = true;
    	int j = i + 1;
		if( j >= docBuffer.length() )
			return result;
		char nextChar = docBuffer.charAt(j);
		while( j <= docBuffer.length()-1 && (nextChar==MCodeConstant.UNI180B_FVS1 
				|| nextChar==MCodeConstant.UNI180C_FVS2 || nextChar==MCodeConstant.UNI180D_FVS3 
				|| nextChar==MCodeConstant.UNI180E_MVS )){
			j++;
			if( j >= docBuffer.length() )
				return result;
			nextChar = docBuffer.charAt(j);
		}
		if( j <= docBuffer.length()-1 && ( nextChar==MCodeConstant.UNI180A_NIRUGU 
				|| ( nextChar >= MCodeConstant.UNI1820_A && nextChar <= MCodeConstant.UNI1842_CHI) ) ) {
			result = false;
		}    	
		return result; 
    }
    
    public static boolean isMongolian( char ch ){
    	
    	if( ch >= MCodeConstant.UNI1820_A && ch <= MCodeConstant.UNI1842_CHI )
    		return true;
    	
    	if( ch == MCodeConstant.UNI180E_MVS || ch <= MCodeConstant.UNI180B_FVS1 
    			|| ch <= MCodeConstant.UNI180C_FVS2 || ch <= MCodeConstant.UNI180D_FVS3 
    			|| ch <= MCodeConstant.UNI180A_NIRUGU )
    		return true;
    		
    	return false;
    }
}
