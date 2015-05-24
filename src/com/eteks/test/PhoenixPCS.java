package com.eteks.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import com.eteks.sweethome3d.io.HomeFileRecorder;
import com.eteks.sweethome3d.model.CatalogPieceOfFurniture;
import com.eteks.sweethome3d.model.CatalogTexture;
import com.eteks.sweethome3d.model.FurnitureCategory;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomeFurnitureGroup;
import com.eteks.sweethome3d.model.HomePieceOfFurniture;
import com.eteks.sweethome3d.model.HomeRecorder;
import com.eteks.sweethome3d.model.HomeTexture;
import com.eteks.sweethome3d.model.Room;
import com.eteks.sweethome3d.model.TexturesCategory;
import com.eteks.sweethome3d.model.Wall;
import com.eteks.sweethome3d.plugin.Plugin;
import com.eteks.sweethome3d.plugin.PluginAction;


public class PhoenixPCS extends Plugin 
{
	public class RoomTestAction extends PluginAction 
	{		

		public Home home = null;
		public Room room = null;
		public Room livingRoom = null;

		public String homeFilepath = "";

		public List<String> furnIds = new ArrayList<String>();
		public List<float[][]> furnRects = new ArrayList<float[][]>();
		public List<float[][]> furnRectsBloated = new ArrayList<float[][]>();	
		public List<Float> furnElevs = new ArrayList<Float>();
		public List<Float> furnHeights = new ArrayList<Float>();
		public List<float[][]> furnRectsAccess = new ArrayList<float[][]>();

		public List<HomePieceOfFurniture> furnList = new ArrayList<HomePieceOfFurniture>();
		public List<List<HomePieceOfFurniture>> catFurnList = new ArrayList<List<HomePieceOfFurniture>>();

		public List<String> wallIds = new ArrayList<String>();
		public List<float[][]> wallRects = new ArrayList<float[][]>();
		public List<Wall> wallLists = new ArrayList<Wall>();

		public List<String> fwsExpIds = new ArrayList<String>();

		public int MARKBOX_COUNT = 8;
		public List<String> markBoxName = new ArrayList<String>();
		public HomePieceOfFurniture[] markBoxes = new HomePieceOfFurniture[MARKBOX_COUNT];		

		public List<String> accBoxName = new ArrayList<String>();
		public HomePieceOfFurniture accBox = null;

		public List<PCSConfig> pcsConfigList = new ArrayList<PCSConfig>();
		public List<String> pcsConfigLabel = new ArrayList<String>();
		
		public List<Design> validDesignList = new ArrayList<Design>();

		public float FURN_TOLERANCE = 0.51f;
		public float ROOM_TOLERANCE = 0.51f;
		public float WALL_TOLERANCE = 0.1f;
		public float ORIENTATION_TOLERANCE = 0.05f;
		public float SLOPE_TOLERANCE = 0.01f;
		public float VALID_INNERWALL_TOLERANCE = 0.5f;		// 5mm		
		public float FURNITURE_PLACE_TOLERANCE = 0.0f; 		//122.0f;	// 4ft 

		public float PLACEMENT_TOLERANCE = 4.0f;	// 4cm
		public float SNAP_TOLERANCE = 76.2f;

		public float tolerance = 1.0f; 				// 5 mm

		public float FURNITURE_BLOAT_SIZE = 5.0f;	// 5cm

		public float TAN_MAX_VAL = 60.0f; 
		public float INFINITY = 10000.0f; 

		public float CONV_IN_M = 100.0f;
		public float CONV_IN_CM = 2.54f;
		public float CONV_FT_CM = (12.0f * CONV_IN_CM);

		public float VALID_RS_LENGTH = (3.0f * CONV_FT_CM);

		public float WALL_HEIGHT = (9.0f * CONV_FT_CM);
		public float WALL_THICKNESS = 11.4f;

		public float DOOR_ELEVATION = (7.0f * CONV_FT_CM);
		public float CENTER_TABLE_HEIGHT = (1.5f * CONV_FT_CM);

		public float ROOM_CONV_SQCM_SQFT = 0.00107639104f;

		public float PCS_RECT_W = 2.5f;
		public float PCS_RECT_D1 = 10.0f;
		public float PCS_RECT_D2 = 11.0f;
		public float PCS_RECT_D3 = 12.0f;

		public float ROOM_AREA_S_MIN = 0.0f;		
		public float ROOM_AREA_S_MAX = 325.0f;

		public float ROOM_AREA_M_MIN = 326.0f;
		public float ROOM_AREA_M_MAX = 500.0f;

		public float ROOM_AREA_L_MIN = 501.0f;
		public float ROOM_AREA_L_MAX = 1000.0f;

		public int accBoxCount = 0;
		public float ACCESSBOX_DEPTH = 2.5f * CONV_FT_CM;
		public float ACCESSBOX_MIN_WIDTH = 2.5f * CONV_FT_CM;
		
		public float ACCESS_CHECK_SIZE = 2.5f * CONV_FT_CM;

		public boolean bShowMarkerInter = false;
		public boolean bShowMarker = false;

		public boolean bShowClearPlacements = false;
		public boolean bDebugMode = true;

		public boolean bIgnoreAccBox = false;

		public Integer orgWallColor = -1184274;
		
		public int MIN_VALID_DESIGN_COUNT = 3;
		public int validDesignCount = 0;

		public String[] dbgArr = new String[5];

		public Wall bckWall = null;

		public String pcsAccBoxNamePrefix = "PCSAccessBox";
		
		public boolean bPopulateFurn = true;
		public boolean bSaveDesign = true;
		
		public float SHIFT_TOLERANCE = 7.5f*CONV_FT_CM; // 6ft
		
		public float PCS_RECT_POS_LEN = 12.0f*CONV_FT_CM;
		
		// ======================= PCS CONSTANTS ======================= //

		public float FOUR_SEATER_INDEX = 0.0f;
		public int[][] FOUR_SEATER_DESIGN_RANGE = {{1,1,0}, {2,2,3}, {2,3,1}};

		public float FIVE_SEATER_INDEX = 1.0f;
		public int[][] FIVE_SEATER_DESIGN_RANGE = {{4,6,1}, {4,7,3}, {4,8,1}, {4,9,3}, {5,5,0}};

		public float SIX_SEATER_INDEX = 2.0f;
		public int[][] SIX_SEATER_DESIGN_RANGE = {{5,14,3}, {5,15,1}, {5,16,3}, {5,17,1}, {6,10,1}, {6,11,3}, {6,12,3}, {6,13,1}};

		public float SEVEN_SEATER_INDEX = 3.0f;
		public int[][] SEVEN_SEATER_DESIGN_RANGE = {{7,19,0}, {7,20,3}, {7,21,1}};

		public float EIGHT_SEATER_INDEX = 4.0f;
		public int[][] EIGHT_SEATER_DESIGN_RANGE = {{8,22,3}, {8,23,1}, {9,26,1}, {9,26,3}};

		public float NINE_SEATER_INDEX = 5.0f;
		public int[][] NINE_SEATER_DESIGN_RANGE = {{11,29,1}, {11,31,1}, {11,31,3}};

		//public float[][][] pcsDimsArr = {{{ROOM_AREA_S_MIN, ROOM_AREA_S_MAX},{SIX_SEATER_INDEX, FIVE_SEATER_INDEX, FOUR_SEATER_INDEX}}, {{ROOM_AREA_M_MIN, ROOM_AREA_M_MAX},{SEVEN_SEATER_INDEX, SIX_SEATER_INDEX, FIVE_SEATER_INDEX}}, {{ROOM_AREA_L_MIN, ROOM_AREA_L_MAX},{NINE_SEATER_INDEX, EIGHT_SEATER_INDEX, SEVEN_SEATER_INDEX, SIX_SEATER_INDEX, FIVE_SEATER_INDEX}}};
		
		public float[] pcsDimsArr = {FIVE_SEATER_INDEX, SIX_SEATER_INDEX, SEVEN_SEATER_INDEX, FOUR_SEATER_INDEX, EIGHT_SEATER_INDEX};
		public int[][][] pcsConfigArr = {FOUR_SEATER_DESIGN_RANGE, FIVE_SEATER_DESIGN_RANGE, SIX_SEATER_DESIGN_RANGE, SEVEN_SEATER_DESIGN_RANGE, EIGHT_SEATER_DESIGN_RANGE, NINE_SEATER_DESIGN_RANGE};

		public String[] configLabelArr = {"4S", "5S", "6S", "7S", "8S", "9S"};

		public String[] seatingTypeArr = {"1_seater_sofa", "2_seater_sofa", "3_seater_sofa", "5_seater_RL_sofa", "5_seater_LL_sofa", "6_seater_RL_sofa", "6_seater_LL_sofa" , "media_cabinet", "settee", "center_table", "corner_table", "area_rug", "4_seater_RL_sofa", "4_seater_LL_sofa", "3_seater_sofa1", "flower_vase"};
		public float[][] seatingDimsArr = {{(2.5f*CONV_FT_CM), (2.5f*CONV_FT_CM)}, {(2.5f*CONV_FT_CM), (2f*2.5f*CONV_FT_CM)}, {(2.5f*CONV_FT_CM), (3.0f*2.5f*CONV_FT_CM)}, {(2f*2.5f*CONV_FT_CM), (3.0f*2.5f*CONV_FT_CM)}, {(2f*2.5f*CONV_FT_CM), (3.0f*2.5f*CONV_FT_CM)}, {(3.0f*2.5f*CONV_FT_CM), (3.0f*2.5f*CONV_FT_CM)}, {(3.0f*2.5f*CONV_FT_CM), (3.0f*2.5f*CONV_FT_CM)}, {(2.0f*2.5f*CONV_FT_CM), (3.0f*2.5f*CONV_FT_CM)}, {(1f*2.5f*CONV_FT_CM), (5f*2.5f*CONV_FT_CM)}, {(3.0f*2.5f*CONV_FT_CM), (5f*2.5f*CONV_FT_CM)}, {(2f*2.5f*CONV_FT_CM), (2f*2.5f*CONV_FT_CM)}, {(4.0f*2.5f*CONV_FT_CM), (6.0f*2.5f*CONV_FT_CM)}, {(2*2.5f*CONV_FT_CM), (3*2.5f*CONV_FT_CM)}, {(2*2.5f*CONV_FT_CM), (3*2.5f*CONV_FT_CM)}, {(2.5f*CONV_FT_CM), (3*2.5f*CONV_FT_CM)}, {(1.25f*CONV_FT_CM), (1.4f*CONV_FT_CM)}};

		public int[] seatingPref = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

		//public String[][] catNamesArr = {{"sofa"}, {"sofa", "sofa 2"}, {"sofa 3", "large couch"}, {"sofa", "couch"}, {"sofa", "couch"}, {"sofa", "couch"}, {"sofa", "couch"}, {"mediacabinet"}, {"chair"}, {"glass table", "coffee table"}, {"round table", "glass table"}, {"rug", "carpet"}};
		public String[][] catNamesArr = {{"1_sofa"}, {"2_sofa"}, {"3_sofa"}, {"3_C_2_R_sofa"}, {"3_C_2_L_sofa"}, {"3_C_3_R_sofa"}, {"3_C_3_L_sofa"}, {"media_cabinet"}, {"settee"}, {"center_table"}, {"corner_table"}, {"area_rug"}, {"3_C_1_R_sofa"}, {"3_C_1_L_sofa"}, {"3_sofa1"}, {"flower_vase"}};

		public String catTextArr = "Gray waves wallpaper";

		// ======================= FLAGS ======================= //	

		public boolean bUseOldApproach = false;		// Place at the middle of valid FWS

		public boolean bUseApproach1 = true;

		public boolean bUseApproach2 = false;

		// ======================= CLASSES ======================= //		

		public class Points
		{
			float x;
			float y;

			public Points()
			{
				x = -10.0f;
				y = -10.0f;
			}

			public Points(float xCoord , float yCoord)
			{
				x = xCoord;
				y = yCoord;
			}
		}				

		public class LineSegement
		{
			Points startP;		// x, y
			Points endP;		// x, y

			public LineSegement(Points sP, Points eP)
			{
				startP = sP;
				endP = eP;
			}

			public LineSegement(WallSegement ws)
			{
				startP = ws.startP;
				endP = ws.endP;
			}
		}	

		public class WallSegement
		{
			Points startP;		// x, y
			Points endP;		// x, y
			float len;

			public WallSegement(Points sP, Points eP, float l)
			{
				startP = sP;
				endP = eP;
				len = l;
			}
		}

		public class Intersect
		{
			Points p;
			float dist;

			public Intersect(Points inP, float inD)
			{
				p = inP;
				dist = inD;
			}
		}

		public class InterPoints
		{
			Points p;
			boolean bOrg;

			public InterPoints(Points inP, boolean inB)
			{
				p = inP;
				bOrg = inB;
			}
		}

		public class FurnLoc
		{
			float w;
			float h;
			float el;
			float ang;
			Points p;

			public FurnLoc(float wIn, float hIn, float elIn, float angIn, Points coord)
			{
				w = wIn;
				h = hIn;
				el = elIn;
				ang = angIn;
				p = coord;
			}

			public FurnLoc()
			{
				w = 0.0f;
				h = 0.0f;
				el = 0.0f;
				ang = 0.0f;
				p = new Points();
			}
		}

		public class Accessibility
		{
			boolean bAddAccess;
			float accessWidth;
			float accessDepth;

			public Accessibility(boolean bAccess, float accW, float accD)
			{
				bAddAccess = bAccess;
				accessWidth = accW;
				accessDepth = accD;
			}
		}

		public class PCSConfig
		{
			float d;
			float w;

			public PCSConfig(float depth , float width)
			{
				d = depth;
				w = width;
			}
		}

		public class Design
		{
			HomeFurnitureGroup pcsGrp;
			int confIndx;
			int seatingIndx;

			public Design(HomeFurnitureGroup fGrp, int cIndx , int sIndx)
			{
				pcsGrp = fGrp;
				confIndx = cIndx;
				seatingIndx = sIndx;
			}
		}

		public RoomTestAction() 
		{
			putPropertyValue(Property.NAME, "PhoenixPCS");
			putPropertyValue(Property.MENU, "Phoenix-Fresh");

			// Enables the action by default
			setEnabled(true);
		}	

		// ======================= CODE ======================= //

		@Override
		public void execute() 
		{	
			home = getHome();
			room = home.getRooms().get(0);

			String hPath = home.getName();
			int indx = hPath.lastIndexOf(".");
			homeFilepath = home.getName().substring(0, indx);

			File f = new File(homeFilepath);

			if(!f.exists())
				f.mkdir();

			try
			{
				init();
				getAccessBox();

				markBoxes = getMarkerBoxes();

				// 1. Add Accessibility boxes for doors ------------//
				// 2. Add Accessibility boxes for windows and wall openings ------------//

				storeAllFurnParams(home);
				
				/* ---->
				storeAllWallRects(home);	

				genConfigList(PCS_RECT_W, PCS_RECT_D1, PCS_RECT_D2, PCS_RECT_D3);

				List<int[][]> activePCSConfList = getLivingConfigs();

				readCatalog();

				long startTime = System.currentTimeMillis(); //System.nanoTime();
				*/
				// ==================== Catalog ========================= //

				// 15. Placement of real furnitures from catalog ------------//
				/*				
				HomePieceOfFurniture f1 = searchMatchFurn("2@3_seater_sofa_17_0");
				HomePieceOfFurniture f2 = searchMatchFurn("2@3_seater_sofa_17_1");
				HomePieceOfFurniture f3 = searchMatchFurn("7@media_cabinet_17_2");

				HomePieceOfFurniture f4 = searchMatchFurn("9@center_table_17_3");
				HomePieceOfFurniture f5 = searchMatchFurn("11@area_rug_17_4");

				populateFurnTest(f5,5);
				populateFurnTest(f4,4);
				populateFurnTest(f3,1);
				populateFurnTest(f2,4);
				populateFurnTest(f1,4);
				 */

				// 4. Config check  ------- //
				/*
				HomePieceOfFurniture hpfTest = searchMatchFurn("PCSRect");

				int[][] testArr = FOUR_SEATER_DESIGN_RANGE;
				testConf(hpfTest, testArr);

				testArr = FIVE_SEATER_DESIGN_RANGE;
				testConf(hpfTest, testArr);

				testArr = SIX_SEATER_DESIGN_RANGE;
				testConf(hpfTest, testArr);

				testArr = SEVEN_SEATER_DESIGN_RANGE;
				testConf(hpfTest, testArr);

				testArr = EIGHT_SEATER_DESIGN_RANGE;
				testConf(hpfTest, testArr);

				testArr = NINE_SEATER_DESIGN_RANGE;
				testConf(hpfTest, testArr);
				*/

				// 5. Increase the length of PCS rect to expand it till the back wall if free  ------- //

				// ==================== Demo ========================= //
				/* ---->
				getLivingRoom();
				float[][] livinRect = livingRoom.getPoints();

				List<WallSegement> innerWSList = getInnerWalls();

				List<WallSegement> validWSList = getValidInnerWallSegmentsOfRoom(innerWSList, livinRect, tolerance);	

				List<WallSegement> fWSList = calcFreeWallIntersectionsBelowElev(validWSList, DOOR_ELEVATION, livingRoom, 1.0f);

				List<WallSegement> finalWSList = shortlistWallSegments(fWSList, VALID_RS_LENGTH);

				validDesignCount = 0;
				//int nameCounter = 1;
				
				for(int c = 0; c < activePCSConfList.size(); c++)
				{		
					int[][] pcsConfArr = activePCSConfList.get(c);
					
					if((c >= 3) && (validDesignCount >= MIN_VALID_DESIGN_COUNT))
					{
						JOptionPane.showMessageDialog(null, "Stopping -> No. of Designs generated : " + validDesignCount);
						break;
					}

					for(int x = 0; x < pcsConfArr.length; x++)
					{								
						int pcsConfIndx = pcsConfArr[x][0];
						int pcsSeatingIndx = pcsConfArr[x][1];
						int pcsWallPrefIndx = pcsConfArr[x][2];

						//JOptionPane.showMessageDialog(null, "Config :" + pcsConfIndx + "," + pcsSeatingIndx + "," + pcsWallPrefIndx);
								
						HomePieceOfFurniture pcsRect = getFurnItem("PCSRect").clone();
						pcsRect.setName("PCSRect_" + pcsConfigLabel.get(c));
						pcsRect.setWidth(pcsConfigList.get(pcsConfIndx).w);
						pcsRect.setDepth(pcsConfigList.get(pcsConfIndx).d);

						placePCSRectWithSnap(finalWSList, pcsRect, innerWSList, validWSList, tolerance, pcsSeatingIndx, pcsWallPrefIndx);

						//nameCounter++;
					}					
				}
				
				long endTime = System.currentTimeMillis(); //System.nanoTime();				
				//JOptionPane.showMessageDialog(null, "No. of Designs generated : " + validDesignCount);

				JOptionPane.showMessageDialog(null, "Time : " + (endTime - startTime) + " ms");
				*/
			}
			catch(Exception e)
			{
				cleanupExp();
				cleanupMarkers();

				JOptionPane.showMessageDialog(null," -x-xxx-x- EXCEPTION : " + e.getMessage());
				//JOptionPane.showMessageDialog(null, "No. of Designs generated : " + validDesignCount);	

				//JOptionPane.showMessageDialog(null," -x-xxx-x- EXCEPTION : " + e.getMessage() + " : " + dbgArr[0]+dbgArr[1]+dbgArr[2]+dbgArr[3]+dbgArr[4]); 
				//e.printStackTrace();
			}			
		}

		public void testConf(HomePieceOfFurniture hpf, int[][] confArr)
		{
			for(int x = 0 ; x < confArr.length; x++)
			{
				PCSConfig conf = pcsConfigList.get(confArr[x][0]);
				hpf.setWidth(conf.w);
				hpf.setDepth(conf.d);

				float[][] hpfRect = hpf.getPoints();
				Points p0 = new Points(hpfRect[0][0], hpfRect[0][1]);
				Points p1 = new Points(hpfRect[1][0], hpfRect[1][1]);
				Points p2 = new Points(hpfRect[2][0], hpfRect[2][1]);

				Points midPX = new Points(((p0.x + p1.x)/2.0f), ((p0.y + p1.y)/2.0f));
				Points midPY = new Points(((p1.x + p2.x)/2.0f), ((p1.y + p2.y)/2.0f));

				hpf.setX(midPX.x);
				hpf.setY(midPY.y);

				int seatIndx = confArr[x][1];

				JOptionPane.showMessageDialog(null, (confArr[x][0]  + 1) + ", " + (confArr[x][1] + 1));
				placeFurnBoxesTest(hpf, seatIndx);
			}
		}

		public void readCatalog()
		{
			for(int s = 0; s < seatingTypeArr.length; s++)
			{						
				float d = seatingDimsArr[s][0];
				float w = seatingDimsArr[s][1];

				List<HomePieceOfFurniture> fList = new ArrayList<HomePieceOfFurniture>();

				for(String fName : catNamesArr[s])
				{
					List<HomePieceOfFurniture> hpfList =  searchCatalog(fName, w, d);
					fList.addAll(hpfList);

					//JOptionPane.showMessageDialog(null, fName + " : " + fList.size() + " >>>> " + w + ", " + d);
				}

				catFurnList.add(s, fList);
			}			
		}		

		public void placeFurnBoxes(HomePieceOfFurniture pcsRect, int seatingIndx, int wallPrefIndx)
		{			
			bckWall = null;

			List<HomePieceOfFurniture> pcsFurnList = new ArrayList<HomePieceOfFurniture>();
			
			float[][] pcsRectP = pcsRect.getPoints();

			Points refOrigin = new Points(pcsRectP[0][0], pcsRectP[0][1]);			
			//putMarkers(refOrigin, 1);

			float pcsAngle = pcsRect.getAngle();

			float[][] seatingConf = getSeatingConfigList(PCS_RECT_W, (pcsRect.getDepth()/CONV_FT_CM), (pcsRect.getWidth()/CONV_FT_CM), seatingIndx);
			//JOptionPane.showMessageDialog(null, "seatingConf : " + seatingIndx + "\n\n" + Arrays.deepToString(seatingConf).replaceAll("],", "],\n"));
			
			for(int f = 0; f < seatingConf.length; f++)
			{						
				int furnType = new Float(seatingConf[f][0]).intValue();
				
				String furnName = seatingTypeArr[furnType];

				float furnX = refOrigin.x + seatingConf[f][1];
				float furnY = refOrigin.y + seatingConf[f][2];
				float furnAng = (((float) (Math.PI * seatingConf[f][3])) / 180.0f);

				//JOptionPane.showMessageDialog(null, furnName + " -> pcsAngle : " + pcsAngle + ", furnAng : " + furnAng);

				HomePieceOfFurniture hpf = getFurnItem(furnName).clone();
				hpf.setName(furnType + "_" + furnName + "_" + seatingIndx + "_" + f);
				hpf.setX(furnX);

				if(furnName.equalsIgnoreCase("media_cabinet"))
				{
					hpf.setY(furnY - (0.5f*hpf.getDepth()));
					//hpf.setWidth(3.0f*CONV_FT_CM);
					//hpf.setDepth(22.0f*CONV_IN_CM);
				}
				else if(furnName.equalsIgnoreCase("flower_vase"))
				{
					hpf.setElevation(seatingConf[f][4]);
					hpf.setY(furnY);
				}
				else
					hpf.setY(furnY);					

				hpf.setAngle(furnAng);

				pcsFurnList.add(hpf);
			}

			HomeFurnitureGroup furnGrp = new HomeFurnitureGroup(pcsFurnList, (pcsRect.getName() + "_Group"));
			List<HomePieceOfFurniture> realFurnList = new ArrayList<HomePieceOfFurniture>();

			furnGrp.setX(pcsRect.getX());
			furnGrp.setY(pcsRect.getY());

			float grpAng = furnGrp.getAngle();
			furnGrp.setAngle(pcsAngle + grpAng);

			for(HomePieceOfFurniture hp : furnGrp.getFurniture())
			{				
				home.addPieceOfFurniture(hp);
			}
			
			String name = pcsRect.getName();
			
			if(bPopulateFurn)
			{
				realFurnList = populateFurn(furnGrp, seatingIndx);
				
				boolean bSuccess = chkAccessibility(pcsRect, realFurnList, tolerance, wallPrefIndx);				
				//JOptionPane.showMessageDialog(null, bSuccess + " : PCS Design generated !!!");
				
				if(bSuccess && realFurnList.size() > 0)
				{
					home.deletePieceOfFurniture(pcsRect);
					cleanupMarkers();
					
					if(bSaveDesign)
						saveDesign(home, (name + "_" + ( + validDesignCount + 1)));
					
					validDesignCount++;
				}
			}
			else
			{
				home.deletePieceOfFurniture(pcsRect);
				
				//JOptionPane.showMessageDialog(null, "PCS Design generated !!!");
				cleanupMarkers();
				
				if(bSaveDesign)
					saveDesign(home, (name + "_" + ( + validDesignCount + 1)));
				
				validDesignCount++;
			}
			
			cleanupRealFurnAndWall(furnGrp.getFurniture(), bckWall);
			cleanupRealFurnAndWall(realFurnList, bckWall);
			cleanupMarkers();
			furnGrp.setAngle(0.0f);
		}
		
		public boolean chkAccessibility(HomePieceOfFurniture inPCSRect, List<HomePieceOfFurniture> furnList, float tolr, int wallPrefIndx)
		{
			boolean bValid = false;			
			float mediaCabDepth = 0.0f;
			
			for(HomePieceOfFurniture furn : furnList)
			{
				String fName = furn.getName();
				
				if(fName.contains("media_cabinet"))
				{
					mediaCabDepth = furn.getDepth();
					break;
				}
			}
			
			List<LineSegement> accLSList = getAccessbilityLineSeg(inPCSRect, ACCESS_CHECK_SIZE, mediaCabDepth, tolr);

			if(accLSList.size() > 1)
			{	
				boolean bIntersects1 = false;
				boolean bIntersects2 = false;
				
				LineSegement accLS1 = accLSList.get(0);
				LineSegement accLS2 = accLSList.get(1);
				
				//putMarkers(accLS1.startP, 3);
				//putMarkers(accLS1.endP, 5);
				//putMarkers(accLS2.startP, 5);
				//putMarkers(accLS2.endP, 6);
				
				for(HomePieceOfFurniture furn : furnList)
				{
					String fName = furn.getName();
					
					HomePieceOfFurniture hpf = searchMatchFurn(furn.getName());
					storeFurnParams(hpf);
					
					List<Intersect> interList1 = checkIntersect(accLS1, fName);
					
					for(Intersect inter : interList1)
					{
						if(inter != null)
						{
							bIntersects1 = checkPointInBetween(inter.p, accLS1.startP, accLS1.endP, tolr);

							if(bIntersects1)
								break;
						}
						//putMarkers(inter.p, 3);
					}
					
					List<Intersect> interList2 = checkIntersect(accLS2, fName);
					
					for(Intersect inter : interList2)
					{
						if(inter != null)
						{
							bIntersects2 = checkPointInBetween(inter.p, accLS1.startP, accLS1.endP, tolr);

							if(bIntersects2)
								break;
						}
						//putMarkers(inter.p, 3);
					}
					
					clearFurnParams(hpf);
				}
				
				//JOptionPane.showMessageDialog(null, "wallPrefIndx : " + wallPrefIndx + ", bIntersects1 : " + bIntersects1 + ", bIntersects2 : " + bIntersects2);
				
				if(wallPrefIndx == 1)
					bValid = (!bIntersects2);
				else if(wallPrefIndx == 3)
					bValid = (!bIntersects1);
				else
					bValid = (!bIntersects1 || !bIntersects2);
			}
			
			return bValid;
		}
		
		public void placeFurnBoxesTest(HomePieceOfFurniture pcsRect, int seatingIndx)
		{
			try
			{
				bckWall = null;
	
				List<HomePieceOfFurniture> pcsFurnList = new ArrayList<HomePieceOfFurniture>();
				List<Integer> refIndxList = new ArrayList<Integer>();
	
				//HomePieceOfFurniture accBox = getFurnItem("accBox").clone();
				HomePieceOfFurniture accBox = getFurnItem("box_invisible").clone();
	
				float[][] pcsRectP = pcsRect.getPoints();
	
				Points refOrigin = new Points(pcsRectP[0][0], pcsRectP[0][1]);			
				//putMarkers(refOrigin, 1);
	
				float pcsAngle = pcsRect.getAngle();
	
				float[][] seatingConf = getSeatingConfigList(PCS_RECT_W, (pcsRect.getDepth()/CONV_FT_CM), (pcsRect.getWidth()/CONV_FT_CM), seatingIndx);
				//JOptionPane.showMessageDialog(null, "seatingConf : " + seatingIndx + "\n\n" + Arrays.deepToString(seatingConf).replaceAll("],", "],\n"));
				
				for(int f = 0; f < seatingConf.length; f++)
				{						
					int furnType = new Float(seatingConf[f][0]).intValue();
					refIndxList.add(furnType);
	
					String furnName = seatingTypeArr[furnType];
	
					float furnX = refOrigin.x + seatingConf[f][1];
					float furnY = refOrigin.y + seatingConf[f][2];
					float furnAng = (((float) (Math.PI * seatingConf[f][3])) / 180.0f);
	
					//JOptionPane.showMessageDialog(null, furnName + " -> pcsAngle : " + pcsAngle + ", furnAng : " + furnAng);
	
					HomePieceOfFurniture hpf = getFurnItem(furnName).clone();
					hpf.setName(furnType + "_" + furnName + "_" + seatingIndx + "_" + f);
					hpf.setX(furnX);
	
					if(furnName.equalsIgnoreCase("media_cabinet"))
					{
						hpf.setY(furnY - (0.5f*hpf.getDepth()));
					}
					else
						hpf.setY(furnY);					
	
					hpf.setAngle(furnAng);
	
					pcsFurnList.add(hpf);
				}
	
				HomeFurnitureGroup furnGrp = new HomeFurnitureGroup(pcsFurnList, (pcsRect.getName() + "_Group"));
				List<HomePieceOfFurniture> realFurnList = new ArrayList<HomePieceOfFurniture>();
	
				furnGrp.setX(pcsRect.getX());
				furnGrp.setY(pcsRect.getY());
	
				float grpAng = furnGrp.getAngle();
				furnGrp.setAngle(pcsAngle + grpAng);
	
				for(HomePieceOfFurniture hp : furnGrp.getFurniture())
				{		
					home.addPieceOfFurniture(hp);
				}
				
				// Real furniture not added
				
				cleanupRealFurnAndWall(furnGrp.getFurniture(), bckWall);
				cleanupRealFurnAndWall(realFurnList, bckWall);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null," -xx||xx- EXCEPTION : " + e.getMessage()); 	
			}
		}

		public List<HomePieceOfFurniture> populateFurn(HomeFurnitureGroup furnGrp, int seatingIndx)
		{		
			List<HomePieceOfFurniture> hpList = new ArrayList<HomePieceOfFurniture>();

			for(int h = 0; h <furnGrp.getFurniture().size(); h++)
			{
				HomePieceOfFurniture hp = furnGrp.getFurniture().get(h);
				String[] nameStr = hp.getName().split("_");

				if(nameStr.length > 0)
				{
					int indx = Integer.parseInt(nameStr[0]);
					
					List<HomePieceOfFurniture> catFurn =  searchCatalog((catNamesArr[indx][0] + "" + seatingIndx), 10.f, 10.f);
					//JOptionPane.showMessageDialog(null, (catNamesArr[indx][0] + "" + seatingIndx));
							
					if(catFurn.size() > 0)
					{
						HomePieceOfFurniture realFurn = catFurn.get(0).clone();
						realFurn.setName("PCS_" + hp.getName());
						realFurn.setX(hp.getX());
						realFurn.setY(hp.getY());
						realFurn.setAngle(hp.getAngle());
	
						// Wallpaper behind Media Cabinet (not now)
						if(indx == 7)	
						{
							Points hpMid = new Points(hp.getX(), hp.getY());						
							populateWallFurn(hpMid, catTextArr, 0);
	
							float newY = hp.getY() + (0.5f*hp.getDepth()) - (0.5f*realFurn.getDepth());
							realFurn.setY(newY);
							
							//realFurn.setWidth(hp.getWidth());
							//realFurn.setDepth(hp.getDepth());
						}
						else if(indx == 15)	
						{
							int objIndx = new Float(hp.getElevation()).intValue();
							
							List<HomePieceOfFurniture> objFurnList =  searchCatalog((catNamesArr[objIndx][0] + "" + seatingIndx), 10.f, 10.f);
							
							if(objFurnList.size() > 0)
							{
								HomePieceOfFurniture objFurn = objFurnList.get(0);
								float elev = objFurn.getHeight();
								
								realFurn.setElevation(elev + 0.2f);
							}							
						}
						
						home.deletePieceOfFurniture(hp);
						
						boolean bIntersects = false;
						storeFurnParams(realFurn);
						
						home.addPieceOfFurniture(realFurn);
						
						if(indx == 7)
						{
							bIntersects = checkIntersectWithAllFurns(realFurn, false, false);							
							clearFurnParams(realFurn);
							
							if(!bIntersects)
								hpList.add(realFurn);
							else
								home.deletePieceOfFurniture(realFurn);
						}
						else
						{
							bIntersects = checkIntersectWithAllFurns(realFurn, false, true);							
							clearFurnParams(realFurn);
							
							if(!bIntersects)
								hpList.add(realFurn);
							else
							{
								hpList.add(realFurn);
								cleanupRealFurnAndWall(hpList, bckWall);
								hpList = new ArrayList<HomePieceOfFurniture>();
								return hpList;
							}
						}
						
						//JOptionPane.showMessageDialog(null, hp.getName() + ", bIntersects : " + bIntersects);					
					}
				}
			}

			//JOptionPane.showMessageDialog(null, "Final - Real Furn!!");
					
			return hpList;
		}
		
		public void populateWallFurn(Points midP, String textName, int prefIndx)
		{				
			float minDist = INFINITY;
			float[][] wRect = new float[0][0];

			for(int w = 0; w < wallLists.size(); w++)
			{
				Wall ws = wallLists.get(w);
				Points wallMidP = new Points(((ws.getXStart() + ws.getXEnd())/2.0f), ((ws.getYStart() + ws.getYEnd())/2.0f));

				float d = calcDistance(wallMidP, midP);

				if(d < minDist)
				{
					minDist = d;
					bckWall = ws;
					wRect = wallRects.get(w);
				}
			}

			Points wallSP = new Points(bckWall.getXStart(), bckWall.getYStart());			
			Points wallEP = new Points(bckWall.getXEnd(), bckWall.getYEnd());
			Points wallMP =  new Points(((wallSP.x + wallEP.x) / 2), ((wallSP.y + wallEP.y) / 2));

			Points w0 = new Points(wRect[0][0], wRect[0][1]);
			Points w1 = new Points(wRect[1][0], wRect[1][1]);
			Points w2 = new Points(wRect[2][0], wRect[2][1]);
			Points w3 = new Points(wRect[3][0], wRect[3][1]);

			List<HomeTexture> htList = searchMatchTexture(textName);

			boolean b1 = checkPointOnSameSide(midP, wallMP, w0, w1);

			if(b1)
			{
				//Points midWS =  new Points(((w0.x + w1.x) / 2), ((w0.y + w1.y) / 2));
				//putMarkers(midWS, 1);

				if(htList.size() > prefIndx)
					bckWall.setRightSideTexture(htList.get(prefIndx));	// apply on right side
				else if(htList.size() > 0)
					bckWall.setRightSideTexture(htList.get(0));
			}

			boolean b2 = checkPointOnSameSide(midP, wallMP, w2, w3);

			if(b2)
			{	
				//Points midWS =  new Points(((w0.x + w1.x) / 2), ((w0.y + w1.y) / 2));
				//putMarkers(midWS, 3);

				if(htList.size() > prefIndx)
					bckWall.setLeftSideTexture(htList.get(prefIndx)); // apply on left side
				else if(htList.size() > 0)
					bckWall.setLeftSideTexture(htList.get(0));
			}

			//JOptionPane.showMessageDialog(null, "b1 : " + b1 + ", b2 : " + b2);
		}

		public void saveDesign(Home h, String name)
		{
			String desName = name.replaceAll("Rect", "Design");
			
			try
			{
				Home hClone = h.clone();
				hClone.setName(desName + "_Home");

				HomeRecorder recorder = new HomeFileRecorder();
				recorder.writeHome(h, (homeFilepath + File.separatorChar + desName  + "_Home" + ".sh3d")); 
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(null, " x-x-x EXCEPTION while saving design !!!");		
			}
		}

		public List<int[][]> getLivingConfigs()
		{	
			List<int[][]> activePCSConfList = new ArrayList<int[][]>();
			pcsConfigLabel = new ArrayList<String>();
			
			float roomSize = room.getArea() * ROOM_CONV_SQCM_SQFT;
			/*
			// ------------ Irrespective of room size try the configs ------------ //
			for(int x = 0 ; x < pcsDimsArr.length; x++)
			{
				if((pcsDimsArr[x][0][0] <= roomSize) && (roomSize <= pcsDimsArr[x][0][1]))
				{							
					for(int y = 0 ; y < pcsDimsArr[x][1].length; y++)
					{
						int indx = new Float(pcsDimsArr[x][1][y]).intValue();

						int[][] activeConfList = pcsConfigArr[indx];
						activePCSConfList.add(activeConfList);
						configLabel.add(configLabelArr[indx]);
					}
				}
			}
			*/
			
			for(int x = 0 ; x < pcsDimsArr.length; x++)
			{
				int indx = new Float(pcsDimsArr[x]).intValue();

				int[][] activeConfList = pcsConfigArr[indx];
				activePCSConfList.add(activeConfList);
				pcsConfigLabel.add(configLabelArr[indx]);
			}
			
			String dbgStr = "roomSize : " + roomSize + " sq. ft. \n\n-------- PCS Configs ---------\n";			

			for(int c = 0; c < activePCSConfList.size(); c++)
			{
				dbgStr += pcsConfigLabel.get(c) + ": \n";		
				int[][] confL = activePCSConfList.get(c);

				for(int x = 0; x < confL.length; x++)
				{
					if(x == 0)
						dbgStr += (confL[x][1]);
					else
						dbgStr += (", " + confL[x][1]);
				}

				dbgStr += "\n\n";
			}
			JOptionPane.showMessageDialog(null, dbgStr);

			return activePCSConfList;
		}

		public void genConfigList(float x, float y1, float y2, float y3)
		{
			pcsConfigList = new ArrayList<PCSConfig>();

			// Config 0 : dummy
			PCSConfig pcsConf = new PCSConfig(0.0f, 0.0f);
			pcsConfigList.add(pcsConf);
						
			// Config 1 : 1 - 10 ft
			pcsConf = new PCSConfig((y1 + x)*CONV_FT_CM, ((4*x) + 1.0f)*CONV_FT_CM);
			pcsConfigList.add(pcsConf);

			// Config 2 : 2,3 - 12.5 ft
			pcsConf = new PCSConfig((y1 + x)*CONV_FT_CM, ((3.0f*x) + 0.5f)*CONV_FT_CM);
			pcsConfigList.add(pcsConf);

			// Config 3 : 4,18 - 12.5 ft
			pcsConf = new PCSConfig((y1 + x)*CONV_FT_CM, ((2.0f*x) + 5.5f)*CONV_FT_CM);
			pcsConfigList.add(pcsConf);

			// Config 4 : 6,7,8,9 - 12.5 ft
			pcsConf = new PCSConfig((y1 + x)*CONV_FT_CM, (4.0f*x)*CONV_FT_CM);
			pcsConfigList.add(pcsConf);

			// Config 5 : 5,14,15,16,17 - 12.5 ft
			pcsConf = new PCSConfig((y1 + x)*CONV_FT_CM, (5.0f*x)*CONV_FT_CM);
			pcsConfigList.add(pcsConf);

			// Config 6 : 10,11,12,13 - 14.5 ft
			pcsConf = new PCSConfig((y3 + x)*CONV_FT_CM, (5.0f*x)*CONV_FT_CM);
			pcsConfigList.add(pcsConf);

			// Config 7 : 19,20,21 - 12.5 ft
			pcsConf = new PCSConfig((y1 + x)*CONV_FT_CM, (5.0f*x)*CONV_FT_CM);
			pcsConfigList.add(pcsConf);

			// Config 8 : 22,23,24,25 - 14.5 ft
			pcsConf = new PCSConfig((y3 + x)*CONV_FT_CM, (5.0f*x)*CONV_FT_CM);
			pcsConfigList.add(pcsConf);

			// Config 9 : 26 - 14.5 ft
			pcsConf = new PCSConfig((y3 + x)*CONV_FT_CM, ((4.0f*x) + 2.0f)*CONV_FT_CM);
			pcsConfigList.add(pcsConf);

			// Config 10 : 27 - 14.5 ft
			pcsConf = new PCSConfig((y3 + x)*CONV_FT_CM, ((4.0f*x) + 2.0f)*CONV_FT_CM);
			pcsConfigList.add(pcsConf);

			// Config 11 : 28,29,30,31,32,33 - 14.5 ft
			pcsConf = new PCSConfig((y3 + x)*CONV_FT_CM, (5.0f*x)*CONV_FT_CM);
			pcsConfigList.add(pcsConf);
		}

		public float[][] getSeatingConfigList(float x, float y, float w, int seatingConfigIndx)
		{
			float[][] retSeatingConfig = new float [0][0];

			switch(seatingConfigIndx)
			{				
				// -------------------------------- 4 Seater -------------------------------- //
				case 1: // Seating Config 1
				{
					float[][] seatingConf1 = {	{0.0f, (x*0.5f)*CONV_FT_CM, (x*1.5f)*CONV_FT_CM, 270.0f},
												{1.0f, (w*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{0.0f, (w - x*0.5f)*CONV_FT_CM, (x*1.5f)*CONV_FT_CM, 90.0f},	
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f} };
	
					retSeatingConfig = (seatingConf1);
					break;
				}
				case 2: // Seating Config 2
				{
					float[][] seatingConf2 = {	{1.0f, (x*0.5f)*CONV_FT_CM, (x*2.14f)*CONV_FT_CM, 270.0f},
												{1.0f, (w - x)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{7.0f, (w - x*1.1f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f + 1.0f)*CONV_FT_CM, (x*2.3f)*CONV_FT_CM, 90.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f + 1.0f)*CONV_FT_CM, (x*2.3f)*CONV_FT_CM, 90.0f} };
	
					retSeatingConfig = (seatingConf2);
					break;
				}
				case 3: // Seating Config 3
				{
	
					float[][] seatingConf3 = {	{1.0f, (x)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{1.0f, (w - x*0.5f)*CONV_FT_CM, (x*2.14f)*CONV_FT_CM, 90.0f},
												{7.0f, (x*1.1f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f - 1.0f)*CONV_FT_CM, (x*2.3f)*CONV_FT_CM, 90.0f},
												{10.0f, (w - x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f - 1.0f)*CONV_FT_CM, (x*2.3f)*CONV_FT_CM, 90.0f} };
	
					retSeatingConfig = (seatingConf3);
					break;
				}
				// -------------------------------- 5 Seater -------------------------------- //
				case 5: // Seating Config 5
				{
					float[][] seatingConf5 = {	{0.0f, (x*0.5f)*CONV_FT_CM, (x*1.65f)*CONV_FT_CM, 270.0f},
												{2.0f, (w*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{0.0f, (w - x*0.5f)*CONV_FT_CM, (x*1.65f)*CONV_FT_CM, 90.0f},	
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{15.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f, 9.0f} };
	
					retSeatingConfig = (seatingConf5);
					break;
				}
				case 6: // Seating Config 6
				{
					float[][] seatingConf6 = {	{2.0f, (x*1.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{1.0f, (w - x*0.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 90.0f},
												{7.0f, (x*1.21f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (x*1.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (x*1.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 0.0f}	};
	
					retSeatingConfig = (seatingConf6);
					break;
				}
				case 7: // Seating Config 7
				{
					float[][] seatingConf7 = {	{1.0f, (x*0.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 270.0f},
												{2.0f, (w - x*1.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{7.0f, (w - x*1.1f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w - x*1.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w - x*1.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 0.0f}	};
	
					retSeatingConfig = (seatingConf7);
					break;
				}
				case 8: // Seating Config 8
				{
					float[][] seatingConf8 = {	{3.0f, (w - x*2.0f)*CONV_FT_CM, (x*1.5f)*CONV_FT_CM, 0.0f},
												{7.0f, (x*1.21f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w - x*2.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{11.0f, (w - x*2.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f}	};
	
					retSeatingConfig = (seatingConf8);
					break;
				}
				case 9: // Seating Config 9
				{
					float[][] seatingConf9 = {	{4.0f, (x*2.0f)*CONV_FT_CM, (3.0f*x*0.5f)*CONV_FT_CM, 0.0f},
												{7.0f, (x*2.6f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (x*2.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f}, 
												{11.0f, (x*2.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f} };
	
					retSeatingConfig = (seatingConf9);
					break;
				}
				// -------------------------------- 6 Seater -------------------------------- //
				case 10: // Seating Config 10
				{
					float[][] seatingConf10 = {	{2.0f, (w - x*2.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{2.0f, (w - x*0.5f)*CONV_FT_CM, (x*2.5f)*CONV_FT_CM, 90.0f},
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w - x*2.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w - x*2.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{15.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f, 10.0f},
												{15.0f, (w - x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f, 10.0f}	};
	
					retSeatingConfig = (seatingConf10);
					break;
				}
				case 11: // Seating Config 11
				{
					float[][] seatingConf11 = {	{2.0f, (x*0.5f)*CONV_FT_CM, (x*2.5f)*CONV_FT_CM, 270.0f},
												{2.0f, (x*2.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (x*2.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (x*2.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{15.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f, 10.0f}};
	
					retSeatingConfig = (seatingConf11);
					break;
				}
				case 12: // Seating Config 12
				{
					float[][] seatingConf12 = {	{6.0f, (x*2.0f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (x*2.5f)*CONV_FT_CM, (x*2.35f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (x*2.5f)*CONV_FT_CM, (x*2.35f)*CONV_FT_CM, 0.0f}	};
						
					retSeatingConfig = (seatingConf12);
					break;
				}
				case 13: // Seating Config 13
				{
					float[][] seatingConf13 = {	{5.0f, (w - x*2.0f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w - x*2.5f)*CONV_FT_CM, (x*2.35f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w - x*2.5f)*CONV_FT_CM, (x*2.35f)*CONV_FT_CM, 0.0f}	};
	
					retSeatingConfig = (seatingConf13);
					break;
				}
				case 14	: // Seating Config 14
				{
					float[][] seatingConf14 = {	{1.0f, (x*0.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 270.0f},
												{2.0f, (w*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{0.0f, (w - x*0.5f)*CONV_FT_CM, (x*1.55f)*CONV_FT_CM, 90.0f},	
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{15.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f, 9.0f}	};
	
					retSeatingConfig = (seatingConf14);
					break;
				}
				case 15: // Seating Config 15
				{
					float[][] seatingConf15 = {	{0.0f, (x*0.5f)*CONV_FT_CM, (x*1.55f)*CONV_FT_CM, 270.0f},
												{2.0f, (w*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{1.0f, (w - x*0.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 90.0f},	
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{15.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f, 9.0f}	};
	
					retSeatingConfig = (seatingConf15);
					break;
				}
				case 16: // Seating Config 16
				{
					float[][] seatingConf16 = {	{12.0f, (w - x*2.0f)*CONV_FT_CM, (x*1.0f)*CONV_FT_CM, 0.0f},
												{1.0f, (x*0.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 90.0f},	
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w - x*2.51f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w - x*2.51f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{15.0f, (w - x*2.51f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f, 9.0f}	};
	
					retSeatingConfig = (seatingConf16);
					break;
				}
				case 17: // Seating Config 17
				{
					float[][] seatingConf17 = {	{1.0f, (w - x*0.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 90.0f},
												{13.0f, (x*2.0f)*CONV_FT_CM, (x*1.0f)*CONV_FT_CM, 0.0f},
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (x*2.51f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (x*2.51f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{15.0f, (x*2.51f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f, 9.0f}	};
	
					retSeatingConfig = (seatingConf17);
					break;
				}
				// -------------------------------- 7 Seater -------------------------------- //
				case 19: // Seating Config 19
				{
					float[][] seatingConf19 = {	{1.0f, (x*0.5f)*CONV_FT_CM, (x*2.5f)*CONV_FT_CM, 270.0f},
												{2.0f, (w*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{1.0f, (w - (x*0.5f))*CONV_FT_CM, (x*2.5f)*CONV_FT_CM, 90.0f},	
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.5f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - (x*0.5f))*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.5f)*CONV_FT_CM, 0.0f}	};
	
					retSeatingConfig = (seatingConf19);
					break;
				}
				case 20: // Seating Config 20
				{
					float[][] seatingConf20 = {	{4.0f, (2.0f*x)*CONV_FT_CM, (x*1.5f)*CONV_FT_CM, 0.0f},
												{1.0f, (w - (x*0.5f))*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 90.0f},	
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.35f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - (x*0.5f))*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.35f)*CONV_FT_CM, 0.0f}	};
	
					retSeatingConfig = (seatingConf20);
					break;
				}
				case 21: // Seating Config 21
				{
					float[][] seatingConf21 = {	{1.0f, (x*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 270.0f},
												{3.0f, (w - (x*2.0f))*CONV_FT_CM, (x*1.5f)*CONV_FT_CM, 0.0f},
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.35f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.35f)*CONV_FT_CM, 0.0f}	};
	
					retSeatingConfig = (seatingConf21);
					break;
				}
				// -------------------------------- 8 Seater -------------------------------- //
				case 22: // Seating Config 22
				{
					float[][] seatingConf22 = {	{2.0f, (x*0.5f)*CONV_FT_CM, (x*2.5f)*CONV_FT_CM, 270.0f},
												{2.0f, (w*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{1.0f, (w - (x*0.5f))*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 90.0f},	
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - (x*0.5f))*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f}	};
	
					retSeatingConfig = (seatingConf22);
					break;
				}
				case 23: // Seating Config 23
				{
					float[][] seatingConf23 = {	{1.0f, (x*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 270.0f},
												{2.0f, (w*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{2.0f, (w - (x*0.5f))*CONV_FT_CM, (5.0f*x*0.5f)*CONV_FT_CM, 90.0f},	
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - (x*0.5f))*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f}	};
						
					retSeatingConfig = (seatingConf23);
					break;
				}
				case 26: // Seating Config 26
				{
					float[][] seatingConf26 = {	{2.0f, (x*0.5f)*CONV_FT_CM, (x*2.65f)*CONV_FT_CM, 270.0f},
												{1.0f, (w*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},	
												{2.0f, (w - (x*0.5f))*CONV_FT_CM, (x*2.65f)*CONV_FT_CM, 90.0f},
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.4f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - (x*0.4f))*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.1f)*CONV_FT_CM, 0.0f}	};
	
					retSeatingConfig = (seatingConf26);
					break;
				}
				// -------------------------------- 9 Seater -------------------------------- //
				case 29: // Seating Config 29
				{
					float[][] seatingConf29 = {	{1.0f, (x*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 270.0f},
												{3.0f, (w - (x*2.0f))*CONV_FT_CM, (x*1.5f)*CONV_FT_CM, 0.0f},
												{8.0f, (w*0.5f)*CONV_FT_CM, (x*3.9f)*CONV_FT_CM, 180.0f},
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{15.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f, 9.0f}	};
						
					retSeatingConfig = (seatingConf29);
					break;
				}
				case 31: // Seating Config 31
				{
					float[][] seatingConf31 = {	{14.0f, (x*0.5f)*CONV_FT_CM, (x*2.6f)*CONV_FT_CM, 270.0f},
												{2.0f, (w*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{2.0f, (w - (x*0.5f))*CONV_FT_CM, (x*2.6f)*CONV_FT_CM, 90.0f},
												{7.0f, (w*0.5f)*CONV_FT_CM, (y)*CONV_FT_CM, 180.0f},
												{9.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{10.0f, (x*0.5f)*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{10.0f, (w - (x*0.5f))*CONV_FT_CM, (x*0.5f)*CONV_FT_CM, 0.0f},
												{11.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f},
												{15.0f, (w*0.5f)*CONV_FT_CM, (x*2.0f)*CONV_FT_CM, 0.0f, 9.0f}	};
	
					retSeatingConfig = (seatingConf31);
					break;
				}
				default:
				{
					JOptionPane.showMessageDialog(null, "-x-x-x- No config to select -x-x-x-");
				}
			}
			
			return retSeatingConfig;
		}
		
		public boolean checkAndSnap(HomePieceOfFurniture hpRef, List<WallSegement> inWSList, float tolr, int wallPrefIndx)
		{		
			Points furnCenter = new Points(hpRef.getX(), hpRef.getY());			
			float[][] fRect = hpRef.getPoints();
			
			boolean bSnapped = false;
			boolean bInvalidConfig = false;
			
			for(int f = 0; f < fRect.length; f++)
			{
				cleanupMarkers();
				
				if(f == 2)
					continue;		// Forward snap not needed
				
				Points startP = new Points(fRect[f][0], fRect[f][1]);
				Points endP = null;

				if(f == (fRect.length - 1))
					endP = new Points(fRect[0][0], fRect[0][1]);
				else
					endP = new Points(fRect[f+1][0], fRect[f+1][1]);
				
				LineSegement fs = new LineSegement(startP, endP);
				
				//putMarkers(startP, 2);
				//putMarkers(endP, 2);
						
				for(WallSegement ws : inWSList)
				{					
					LineSegement ls = new LineSegement(ws);
					
					boolean bIsParallel = isParallelToFace(fs, ls, tolr);
					
					if(bIsParallel)
					{
						Points wsMidP = new Points(((ls.startP.x + ls.endP.x)/2),(ls.startP.y + ls.endP.y)/2);
						//putMarkers(wsMidP, 6);
						
						float dist = calcDistanceParallel(fs, ls, tolr);						
						//JOptionPane.showMessageDialog(null, dist + " -> " + f);
								
						if(dist <= SNAP_TOLERANCE)
						{
							List<Points> snapPList = calcSnapCoordinate(ls, fs, dist, livingRoom, tolr);
							
							for(Points snapP : snapPList)
							{
								Points centerFS = new Points(((fs.startP.x + fs.endP.x)/2.0f),(fs.startP.y + fs.endP.y)/2.0f);								
								
								Points snapCoords = new Points((snapP.x - centerFS.x), (snapP.y - centerFS.y));	
								
								hpRef.setX(furnCenter.x + snapCoords.x);
								hpRef.setY(furnCenter.y + snapCoords.y);
								
								//putMarkers(new Points(hpRef.getX(), hpRef.getY()), 6);
								
								boolean bValid = false;
								
								boolean bLiesOnWall = checkFace(hpRef.getPoints(), f, ls, tolr);
								
								//JOptionPane.showMessageDialog(null, "1. bLiesOnWall : " + bLiesOnWall + ", " + f);
								
								boolean bFrontLiesOnWall = false;
								
								if((f == 0) && bLiesOnWall)
								{
									for(WallSegement ws2 : inWSList)
									{	
										LineSegement ls2 = new LineSegement(ws2);
										bFrontLiesOnWall = checkFace(hpRef.getPoints(), 2, ls2, tolr);
										
										if(bFrontLiesOnWall)
											break;
									}
									
									if(!bFrontLiesOnWall)
										bLiesOnWall = false;
								}
								
								//JOptionPane.showMessageDialog(null, "2. bLiesOnWall : " + bLiesOnWall + ", " + f);
										
								if(!bLiesOnWall)
								{
									hpRef.setX(furnCenter.x);
									hpRef.setY(furnCenter.y);
								}								
								else
									bValid = checkInsideRoom(livingRoom, hpRef.getPoints(), PLACEMENT_TOLERANCE);								
								
								//JOptionPane.showMessageDialog(null, "bValid : " + bValid + ", " + f);
								
								if(bValid)
								{
									//JOptionPane.showMessageDialog(null, "checkAndSnap - bValid : " + bValid);
									
									furnCenter = new Points(hpRef.getX(), hpRef.getY());																		
									//Points p = new Points(furnCenter.x, furnCenter.y);
									//putMarkers(p, 3);
									
									if(f != 0)
									{
										bSnapped = true;
										//JOptionPane.showMessageDialog(null, "Snappedddd"  + ", " + f);
										
										if((wallPrefIndx != 0) && (wallPrefIndx != f))
										{
											bInvalidConfig = true;
											home.deletePieceOfFurniture(hpRef);
										}
										
										break;
									}								
								}
								else
								{
									hpRef.setX(furnCenter.x);
									hpRef.setY(furnCenter.y);
								}
							}								
						}
					}
					
					if(bSnapped)
						break;				
				}
				
				if(bSnapped)
					break;
			}
			
			boolean bValid = true;
			
			if(bInvalidConfig)
				bValid = false;

			//JOptionPane.showMessageDialog(null, "Snap : Final placement -> " + bValid + " (" + bInvalidConfig + ")");
			
			return bValid;
		}

		public List<HomePieceOfFurniture> checkAndShift(HomePieceOfFurniture pcsRect, List<WallSegement> inWSList, float tolr, int wallPrefIndx)
		{
			cleanupMarkers();			
			List<HomePieceOfFurniture> shiftedRectList = new ArrayList<HomePieceOfFurniture>();
			
			//JOptionPane.showMessageDialog(null, "Initial position");
			
			Points furnCenter = new Points(pcsRect.getX(), pcsRect.getY());
			float[][] fRect = pcsRect.getPoints();

			for(int f = 0; f < fRect.length; f++)
			{
				if(f == 2)
					continue;				// Forward shift not needed
				
				if((f == 1) || (f == 3))
					continue;				// Disabling sideway shift for now (beta)

				Points startP = new Points(fRect[f][0], fRect[f][1]);
				Points endP = null;

				if(f == (fRect.length - 1))
					endP = new Points(fRect[0][0], fRect[0][1]);
				else
					endP = new Points(fRect[f+1][0], fRect[f+1][1]);

				LineSegement fs = new LineSegement(startP, endP);
				//putMarkers(startP, 2);
				//putMarkers(endP, 2);

				TreeMap<Float, WallSegement> distMap = new TreeMap<Float, WallSegement>();
				
				for(WallSegement ws : inWSList)
				{					
					LineSegement ls = new LineSegement(ws);

					boolean bIsParallel = isParallelToFace(fs, ls, tolr);

					if(bIsParallel)
					{
						Points wsMidP = new Points(((ls.startP.x + ls.endP.x)/2),(ls.startP.y + ls.endP.y)/2);
						//putMarkers(wsMidP, 7);

						boolean bOnSameSide = checkPointOnSameSide(wsMidP, furnCenter, fs.startP, fs.endP);
						
						if(!bOnSameSide)
							ws.len = 1.0f;
						else
							ws.len = -1.0f;
						
						Float dist = calcDistanceParallel(fs, ls, tolr);
						distMap.put(dist, ws);
					}		
				}

				if(distMap.keySet().size() < 1)
					continue;
				
				List<Float> minKeys = new ArrayList<Float>();
				
				Set<Float> keys = distMap.keySet();
				
				for(Float k : keys)
				{
					if(minKeys.size() > 2)
						break;
					
					minKeys.add(k);
				}
				
				for(Float minDist : minKeys)
				{
					WallSegement minWS = distMap.get(minDist);
					LineSegement minLS = new LineSegement(minWS);
					
					//Points midMinLS = new Points(((minLS.startP.x + minLS.endP.x)/2.0f),(minLS.startP.y + minLS.endP.y)/2.0f);	
					//putMarkers(midMinLS, 5);
					
					if(minDist > SHIFT_TOLERANCE)
						continue;
					
					List<Points> shiftPList = calcSnapCoordinate(minLS, fs, minDist, livingRoom, tolr);

					int markerCount = 1;
					
					for(Points shiftP : shiftPList)
					{
						HomePieceOfFurniture hpRef = pcsRect.clone();
						
						//putMarkers(snapP, 3);						
						Points centerFS = new Points(((fs.startP.x + fs.endP.x)/2.0f),(fs.startP.y + fs.endP.y)/2.0f);								

						Points shiftCoords = new Points();
						
						boolean bShiftInwards = (minWS.len == -1.0f) ? true : false;
						
						if(bShiftInwards)
							shiftCoords = new Points((centerFS.x - shiftP.x), (centerFS.y - shiftP.y));
						else
							shiftCoords = new Points((shiftP.x - centerFS.x), (shiftP.y - centerFS.y));

						if(bShiftInwards)
						{
							hpRef.setX(furnCenter.x - shiftCoords.x);
							hpRef.setY(furnCenter.y - shiftCoords.y);
						}
						else
						{
							hpRef.setX(furnCenter.x + shiftCoords.x);
							hpRef.setY(furnCenter.y + shiftCoords.y);
						}					

						if(markerCount == 1)
							putMarkers(new Points(hpRef.getX(), hpRef.getY()), 3);
						else
							putMarkers(new Points(hpRef.getX(), hpRef.getY()), 1);
					
						putMarkers(furnCenter, 2);
						
						boolean bValid = true;
						
						if(!bShiftInwards)
							bValid = checkInsideRoom(livingRoom, hpRef.getPoints(), PLACEMENT_TOLERANCE);

						//JOptionPane.showMessageDialog(null, "bV : " + bValid);
							
						if(bValid)
						{
							float currX = hpRef.getX();
							float currY = hpRef.getY();

							float newX = 0.0f;
							float newY = 0.0f;

							newX = (furnCenter.x + currX) / 2.0f;
							newY = (furnCenter.y + currY) / 2.0f;
							
							hpRef.setX(newX);
							hpRef.setY(newY);

							float elongLen = calcDistance(new Points(currX, currY), furnCenter);
							
							float currDep = hpRef.getDepth();
							
							if(bShiftInwards)
							{
								hpRef.setDepth(currDep - elongLen);
								//JOptionPane.showMessageDialog(null, "Shift -> D-- !!!");
							}
							else
							{
								hpRef.setDepth(currDep + elongLen);
								//JOptionPane.showMessageDialog(null, "Shift -> D++ !!!");
							}
							
							float[][] elongFRect = hpRef.getPoints();
							Points elongStartP = new Points(elongFRect[0][0], elongFRect[0][1]);
							Points elongEndP = new Points(elongFRect[0][0], elongFRect[0][1]);

							LineSegement elongFS = new LineSegement(elongStartP, elongEndP);
							
							float paraDist = calcDistanceParallel(minLS, elongFS, tolr);
							//JOptionPane.showMessageDialog(null, "paraDist : " + paraDist);
							
							boolean bValidShift = (paraDist <= PLACEMENT_TOLERANCE) ? true : false;
							
							if(bValidShift)
							{
								if(checkInsideRoom(livingRoom, hpRef.getPoints(), PLACEMENT_TOLERANCE))
								{
									//bShiftedD = true;
									//furnCenter = new Points(hpRef.getX(), hpRef.getY());									
									//putMarkers(furnCenter, 5);
									
									shiftedRectList.add(hpRef.clone());
								}
							}
						}
					
						markerCount++;
					}
				}
			}
			
			//JOptionPane.showMessageDialog(null, "Finally Here @@");
			
			return shiftedRectList;		
		}

		public boolean checkFace(float[][] fRect, int indx, LineSegement ls, float tolr)
		{
			boolean bLiesOnWall = false;
			cleanupMarkers();
			
			Points fStartP = new Points();
			Points fEndP = new Points();
			
			if(indx == 0)
			{
				fStartP = new Points(fRect[0][0], fRect[0][1]);
				fEndP = new Points(fRect[1][0], fRect[1][1]);
			}			
			else if(indx == 1)
			{
				fStartP = new Points(fRect[1][0], fRect[1][1]);
				fEndP = new Points(fRect[2][0], fRect[2][1]);
			}
			else if(indx == 2)
			{
				fStartP = new Points(fRect[2][0], fRect[2][1]);
				fEndP = new Points(fRect[3][0], fRect[3][1]);
			}
			else if(indx == 3)
			{
				fStartP = new Points(fRect[3][0], fRect[3][1]);				
				fEndP = new Points(fRect[0][0], fRect[0][1]);
			}
			
			//putMarkers(fEndP, 3);
			//putMarkers(fStartP, 4);
			
			Points centerLS = new Points(((ls.startP.x + ls.endP.x)/2.0f),(ls.startP.y + ls.endP.y)/2.0f);
			//putMarkers(centerLS, 1);
			
			Points centerFS = new Points(((fStartP.x + fEndP.x)/2.0f),(fStartP.y + fEndP.y)/2.0f);
			
			//  X-------F==========F-------X
			boolean b1 = checkPointInBetween(fStartP, ls.startP, ls.endP, tolr);
			boolean b2 = checkPointInBetween(fEndP, ls.startP, ls.endP, tolr);
			boolean b3 = checkPointInBetween(centerFS, ls.startP, ls.endP, tolr);
			
			//JOptionPane.showMessageDialog(null, "1. b1 : " + b1 + ", b2 : " + b2 + ", b3 : " + b3);
			
			//if(b1 || b2 || b3)
				//bLiesOnWall = true;
			
			if((b1 && b2) || (b2 && b3) || (b3 && b1))
				bLiesOnWall = true;

			cleanupMarkers();
			return bLiesOnWall;
		}
		
		/*
		public boolean checkFace(float[][] fRect, int indx, List<WallSegement> inWSList, float tolr)
		{
			boolean bLiesOnWall = false;
			cleanupMarkers();
			
			Points fStartP = new Points();
			Points fEndP = new Points();
			
			if(indx == 0)
			{
				fStartP = new Points(fRect[0][0], fRect[0][1]);
				putMarkers(fStartP, 4);
				
				fEndP = new Points(fRect[1][0], fRect[1][1]);
				putMarkers(fEndP, 3);
			}			
			else if(indx == 1)
			{
				fStartP = new Points(fRect[1][0], fRect[1][1]);
				putMarkers(fStartP, 4);
				
				fEndP = new Points(fRect[2][0], fRect[2][1]);
				putMarkers(fEndP, 3);
			}
			else if(indx == 2)
			{
				fStartP = new Points(fRect[2][0], fRect[2][1]);
				putMarkers(fStartP, 4);
				
				fEndP = new Points(fRect[3][0], fRect[3][1]);
				putMarkers(fEndP, 3);
			}
			else if(indx == 3)
			{
				fStartP = new Points(fRect[3][0], fRect[3][1]);
				putMarkers(fStartP, 4);
				
				fEndP = new Points(fRect[0][0], fRect[0][1]);
				putMarkers(fEndP, 3);
			}			

			for(WallSegement ws : inWSList)
			{
				LineSegement ls = new LineSegement(ws);
				
				Points centerWS = new Points(((ls.startP.x + ls.endP.x)/2.0f),(ls.startP.y + ls.endP.y)/2.0f);
				putMarkers(centerWS, 1);
				
				//  X-------F==========F-------X
				boolean b11 = checkPointInBetween(fStartP, ls.startP, ls.endP, tolr);
				boolean b21 = checkPointInBetween(fEndP, ls.startP, ls.endP, tolr);

				if(b11 && b21)
				{
					bLiesOnWall = true;
					break;
				}
				
				JOptionPane.showMessageDialog(null, "1. b1 : " + b11 + ", b2 : " + b21);

				//  F========X==========F-------X
				boolean b12 = checkPointInBetween(ls.startP, fStartP, fEndP, tolr);				
				boolean b22 = checkPointInBetween(fEndP, ls.startP, ls.endP, tolr);
				
				JOptionPane.showMessageDialog(null, "2. b1 : " + b12 + ", b2 : " + b22);

				if(b12 && b22)
				{
					bLiesOnWall = true;
					break;
				}
				
				//  X-------F==========X=========F
				boolean b13 = checkPointInBetween(ls.endP, fStartP, fEndP, tolr);				
				boolean b23 = checkPointInBetween(fStartP, ls.startP, ls.endP, tolr);
				
				JOptionPane.showMessageDialog(null, "3. b1 : " + b13 + ", b2 : " + b23);

				if(b13 && b23)
				{
					bLiesOnWall = true;
					break;
				}
				
				//  XF=======0=========F-------X
				//  X-------F=======0=========FX
			}

			cleanupMarkers();
			return bLiesOnWall;
		}
		*/
		
		public void placePCSRectWithSnap(List<WallSegement> finalWSList, HomePieceOfFurniture pcsRect, List<WallSegement> inWSList, List<WallSegement> validWSList, float tolr, int pcsSeatingIndx, int pcsWallPrefIndx)
		{
			boolean bSuccess = false;

			int counter1 = 1; 

			for(WallSegement ws : finalWSList)
			{				
				LineSegement ls = new LineSegement(ws);

				Points midWS = new Points(((ls.startP.x + ls.endP.x)/2.0f),((ls.startP.y + ls.endP.y)/2.0f));
				//putMarkers(midWS, 6);

				HomePieceOfFurniture hpfP = pcsRect.clone();
				hpfP.setName(pcsRect.getName() + "_" + counter1);

				List<Points> pcsPointList = calculateRectPos(ws, hpfP);

				int counter2 = 1; 
				
				for (Points pcsPoint : pcsPointList)
				{
					HomePieceOfFurniture hpPlaced = hpfP.clone();
					hpPlaced.setName(hpfP.getName() + "_" + counter2);
					
					placeFurnParallelToWall(ls, hpPlaced, pcsPoint);
					bIgnoreAccBox = true;						
					chkFurnOrient(hpPlaced, ws, tolerance);		// returns orientation (180*)

					checkAndSnap(hpPlaced, inWSList, tolr, pcsWallPrefIndx);

					List<HomePieceOfFurniture> hpShiftList = checkAndShift(hpPlaced, inWSList, tolr, pcsWallPrefIndx);
					
					String rectName = hpPlaced.getName();
					home.deletePieceOfFurniture(hpPlaced);
					
					int counter3 = 1; 
					
					for(HomePieceOfFurniture hpShift : hpShiftList)
					{
						hpShift.setName(rectName + "_" + counter3);
						home.addPieceOfFurniture(hpShift);
						
						bSuccess = checkInsideHome(finalWSList, hpShift, PLACEMENT_TOLERANCE);
						//JOptionPane.showMessageDialog(null, "bSuccess : " + bSuccess);

						if(bSuccess)
						{	
							//JOptionPane.showMessageDialog(null, "bSuccess : " + bSuccess);
							placeFurnBoxes(hpShift, pcsSeatingIndx, pcsWallPrefIndx);
						}
						else
							home.deletePieceOfFurniture(hpShift);
						
						counter3++; 
					}					
									
					counter2++;
				}
				
				home.deletePieceOfFurniture(hpfP);
				counter1++;
			}
		}

		public List<Points> calculateRectPos(WallSegement ws, HomePieceOfFurniture pcsRectClone)
		{
			List<Points> rectPos = new ArrayList<Points>();
			cleanupMarkers();
			
			int entryCount = 0;
			
			if(bUseOldApproach)
			{
				rectPos.add(calcFurnMids(ws.startP, ws.endP, (0.5f * pcsRectClone.getDepth()), livingRoom));
			}
			else if (bUseApproach1)		// TODO : Test these
			{
				TreeMap<Float, Points> rectPosMap = new TreeMap<Float, Points>(); 
				
				if(ws.len >= PCS_RECT_POS_LEN)
				{
					int incr = new Float(ws.len / PCS_RECT_POS_LEN).intValue();

					Points startP = ws.startP;

					for(int i = 0; i < incr; i++)
					{
						List<Points> incrPListS = getIntersectionCircleLine(startP, (PCS_RECT_POS_LEN * (i+1)), ws.startP, ws.endP);

						for (Points pS : incrPListS)
						{
							boolean bInside = checkPointInBetween(pS, startP, ws.endP, tolerance);

							if(bInside)
							{
								Points p = calcFurnMids(startP, pS, (0.5f * pcsRectClone.getDepth()), livingRoom);
								
								Float d = calcDistance(p, ws.startP);
								rectPosMap.put(d, p);
								entryCount++;
								
								//--->rectPos.add(p);
								//putMarkers(p, 1);
							}
						}
					}
					
					startP = ws.endP;

					for(int i = 0; i < incr; i++)
					{
						List<Points> incrPListE = getIntersectionCircleLine(startP, (PCS_RECT_POS_LEN * (i+1)), ws.startP, ws.endP);

						for (Points pE : incrPListE)
						{
							boolean bInside = checkPointInBetween(pE, startP, ws.startP, tolerance);

							if(bInside)
							{
								Points p = calcFurnMids(startP, pE, (0.5f * pcsRectClone.getDepth()), livingRoom);
								
								Float d = calcDistance(p, ws.startP);
								rectPosMap.put(d, p);
								entryCount++;
								
								//--->rectPos.add(p);
								//putMarkers(p, 2);
							}
						}
					}
				}
				else
				{
					Points p = calcFurnMids(ws.startP, ws.endP, (0.5f * pcsRectClone.getDepth()), livingRoom);
					
					Float d = calcDistance(p, ws.startP);
					rectPosMap.put(d, p);
					entryCount++;
					
					//--->rectPos.add(p);
					//putMarkers(p, 3);
				}
				
				Set<Float> distSet = rectPosMap.keySet();
				
				for(Float d : distSet)
				{
					rectPos.add(rectPosMap.get(d));
				}
				
				//JOptionPane.showMessageDialog(null, "1. entryCount : " + entryCount + ", finalCount : " + rectPos.size()); 
			}
			else if(bUseApproach2)
			{
				TreeMap<Float, Points> rectPosMap = new TreeMap<Float, Points>(); 
				
				float rectW = pcsRectClone.getWidth();
				
				if(ws.len >= rectW)
				{
					int incr = new Float(ws.len / rectW).intValue();

					Points startP = ws.startP;

					for(int i = 0; i < incr; i++)
					{
						List<Points> incrPListS = getIntersectionCircleLine(startP, (PCS_RECT_POS_LEN * (i+1)), ws.startP, ws.endP);

						for (Points pS : incrPListS)
						{
							boolean bInside = checkPointInBetween(pS, startP, ws.endP, tolerance);

							if(bInside)
							{
								Points p = calcFurnMids(startP, pS, (0.5f * pcsRectClone.getDepth()), livingRoom);
								
								Float d = calcDistance(p, ws.startP);
								rectPosMap.put(d, p);
								entryCount++;
								
								//--->rectPos.add(p);
								//putMarkers(p, 1);
							}
						}
					}
					
					startP = ws.endP;

					for(int i = 0; i < incr; i++)
					{
						List<Points> incrPListE = getIntersectionCircleLine(startP, (PCS_RECT_POS_LEN * (i+1)), ws.startP, ws.endP);

						for (Points pE : incrPListE)
						{
							boolean bInside = checkPointInBetween(pE, startP, ws.startP, tolerance);

							if(bInside)
							{
								Points p = calcFurnMids(startP, pE, (0.5f * pcsRectClone.getDepth()), livingRoom);
								
								Float d = calcDistance(p, ws.startP);
								rectPosMap.put(d, p);
								entryCount++;
								
								//--->rectPos.add(p);
								//putMarkers(p, 2);
							}
						}
					}
				}
				else
				{
					Points p = calcFurnMids(ws.startP, ws.endP, (0.5f * pcsRectClone.getDepth()), livingRoom);
					
					Float d = calcDistance(p, ws.startP);
					rectPosMap.put(d, p);
					entryCount++;
					
					//--->rectPos.add(p);
					//putMarkers(p, 3);
				}
				
				Set<Float> distSet = rectPosMap.keySet();
				
				for(Float d : distSet)
				{
					rectPos.add(rectPosMap.get(d));
				}
				
				JOptionPane.showMessageDialog(null, "2. entryCount : " + entryCount + ", finalCount : " + rectPos.size()); 
			}

			//JOptionPane.showMessageDialog(null, "calculateRectPos");
			cleanupMarkers();
			
			return rectPos;
		}

		public List<LineSegement> getAccessbilityLineSeg(HomePieceOfFurniture inPCSRect, float accDist, float mcDist, float tolr)
		{
			List<LineSegement> accLSList = new ArrayList<LineSegement>();

			float[][] fRect = inPCSRect.getPoints();

			Points startP1 = new Points(fRect[2][0], fRect[2][1]);
			Points endP1 = new Points(fRect[1][0], fRect[1][1]);
			
			Points startP2 = new Points(fRect[3][0], fRect[3][1]);
			Points endP2 = new Points(fRect[0][0], fRect[0][1]);
			
			// =========== Side 1 : 2 -> 1 =========== //
			
			Points mcEndP1 = new Points();
			
			List<Points> mcEndPList1 = getIntersectionCircleLine(startP1, mcDist, startP1, endP1);

			for(Points p : mcEndPList1)
			{				
				if(checkPointOnSameSide(p, endP1, startP1, startP2))
					mcEndP1 = p;
			}

			if(bShowMarkerInter)
				putMarkers(mcEndP1, 0);
			
			List<Points> accPList1 = getIntersectionCircleLine(mcEndP1, accDist, startP1, endP1);

			for(Points p : accPList1)
			{				
				if(checkPointOnSameSide(p, endP1, startP1, startP2))
				{
					accLSList.add(new LineSegement(mcEndP1, p));
					
					//if(bShowMarkerInter)
						putMarkers(p, 1);
				}
			}			
			
			// =========== Side 3 : 3 -> 0 =========== //
			
			Points mcEndP2 = new Points();

			List<Points> mcEndPList2 = getIntersectionCircleLine(startP2, mcDist, startP2, endP2);

			for(Points p : mcEndPList2)
			{				
				if(checkPointOnSameSide(p, endP1, startP1, startP2))
					mcEndP2 = p;
			}

			if(bShowMarkerInter)
				putMarkers(mcEndP2, 0);
			
			List<Points> accPList2 = getIntersectionCircleLine(mcEndP2, accDist, startP2, endP2);

			for(Points p : accPList2)
			{				
				if(checkPointOnSameSide(p, endP2, startP1, startP2))
				{
					accLSList.add(new LineSegement(mcEndP2, p));
					
					//if(bShowMarkerInter)
						putMarkers(p, 2);
				}
			}

			return accLSList;
		}

		public List<WallSegement> shortlistWallSegments(List<WallSegement> inWSList, float reqLen)
		{
			List<WallSegement> finalWSList = new ArrayList<WallSegement>();

			for(WallSegement ws : inWSList)
			{
				if(ws.len >= reqLen)
				{
					finalWSList.add(ws);
					
					if(bShowMarker)
					{
						Points midFWS = new Points(((ws.startP.x + ws.endP.x)/2.0f),((ws.startP.y + ws.endP.y)/2.0f));
						//putMarkers(midFWS, 1);
						//JOptionPane.showMessageDialog(null, "!!!");
					}
				}
			}

			return finalWSList;
		}

		public List<WallSegement> getValidInnerWallSegmentsOfRoom(List<WallSegement> innerWSList, float[][] roomRect, float tolr)
		{
			List<WallSegement> validRSWallList = new ArrayList<WallSegement>();

			for(int l = 0; l < roomRect.length; l++)
			{					
				Points startP = new Points(roomRect[l][0], roomRect[l][1]);
				Points endP = null;

				if(l == (roomRect.length - 1))
					endP = new Points(roomRect[0][0], roomRect[0][1]);
				else
					endP = new Points(roomRect[l+1][0], roomRect[l+1][1]);

				Points midP = new Points(((startP.x + endP.x)/2.0f),((startP.y + endP.y)/2.0f));
				LineSegement rs = new LineSegement(startP, endP);

				List<WallSegement> validList = new ArrayList<WallSegement>();

				for(WallSegement ws : innerWSList)
				{						
					LineSegement ls = new LineSegement(ws);
					boolean bIsParallel = isParallel(rs, ls, tolr);

					if(bIsParallel)
					{										
						float dist = calcDistancePointLine(midP, ls, tolr);

						if(dist <= VALID_INNERWALL_TOLERANCE)
						{
							validList.add(ws);

							if(bShowMarkerInter)
							{
								// Marker
								Points midWS = new Points(((ws.startP.x + ws.endP.x)/2.0f),((ws.startP.y + ws.endP.y)/2.0f));
								//putMarkers(midWS, 0);
							}

						}
					}
				}

				float lenRS = calcDistance(rs.startP, rs.endP);

				for(WallSegement ws : validList)
				{
					if(lenRS > ws.len)
					{	
						List<WallSegement> validRSWallPieceList = new ArrayList<WallSegement>();

						boolean bWSInRS1 = checkPointInBetween(ws.startP, rs.startP, rs.endP, tolr);

						//JOptionPane.showMessageDialog(null, "ws : " + ws.startP.x + ", " + ws.startP.y + " [" + ws.len + "] -> " + lenRS);

						if(bWSInRS1)
						{
							boolean bWSInRS2 = checkPointInBetween(ws.endP, rs.startP, rs.endP, tolr);

							if(bWSInRS2)
							{											
								validRSWallPieceList.add(ws);

								if(bShowMarkerInter)
								{
									// Marker
									//Points midWS = new Points(((ws.startP.x + ws.endP.x)/2.0f),((ws.startP.y + ws.endP.y)/2.0f));
									//putMarkers(midWS, 3);
								}
							}
							else
							{
								boolean bWSOverlapRS = checkPointInBetween(rs.endP, ws.startP, ws.endP, tolr);

								if(bWSOverlapRS)
								{
									float len = calcDistance(ws.startP, rs.endP);

									WallSegement newRS = new WallSegement(ws.startP, rs.endP, len);
									validRSWallPieceList.add(newRS);

									if(bShowMarkerInter)
									{
										// Marker
										//Points midWS = new Points(((newRS.startP.x + newRS.endP.x)/2.0f),((newRS.startP.y + newRS.endP.y)/2.0f));
										//putMarkers(midWS, 5);
									}
								}
							}
						}
						else
						{
							boolean bWSEInRS1 = checkPointInBetween(ws.endP, rs.startP, rs.endP, tolr);

							if(bWSEInRS1)
							{
								boolean bWSEInRS2 = checkPointInBetween(ws.startP, rs.startP, rs.endP, tolr);

								if(bWSEInRS2)
								{											
									validRSWallPieceList.add(ws);

									if(bShowMarkerInter)
									{
										// Marker
										//Points midWS = new Points(((ws.startP.x + ws.endP.x)/2.0f),((ws.startP.y + ws.endP.y)/2.0f));
										//putMarkers(midWS, 3);
									}
								}
								else
								{
									boolean bWSOverlapRS = checkPointInBetween(rs.endP, ws.endP, ws.endP, tolr);

									if(bWSOverlapRS)
									{
										float len = calcDistance(ws.endP, rs.endP);

										WallSegement newRS = new WallSegement(ws.endP, rs.endP, len);
										validRSWallPieceList.add(newRS);

										if(bShowMarkerInter)
										{
											// Marker
											//Points midWS = new Points(((newRS.startP.x + newRS.endP.x)/2.0f),((newRS.startP.y + newRS.endP.y)/2.0f));
											//putMarkers(midWS, 5);
										}
									}
								}

							}
							else
							{
								boolean bRSInWS1 = checkPointInBetween(rs.startP, ws.startP, ws.endP, tolr);

								if(bRSInWS1)
								{
									Points sP = rs.startP;

									boolean bWSInRS2 = checkPointInBetween(ws.endP, rs.startP, rs.endP, tolr);

									if(bWSInRS2)
									{
										float len = calcDistance(sP, ws.endP);

										WallSegement newRS = new WallSegement(sP, ws.endP, len);
										validRSWallPieceList.add(newRS);

										if(bShowMarkerInter)
										{
											// Marker
											Points midWS = new Points(((newRS.startP.x + newRS.endP.x)/2.0f),((newRS.startP.y + newRS.endP.y)/2.0f));
											//putMarkers(midWS, 4);
										}
									}
								}
							}
						}

						// Concatenate valid pieces of RS
						for(WallSegement rsPiece : validRSWallPieceList)
						{
							validRSWallList.add(rsPiece);

							if(bShowMarkerInter)
							{
								// Marker
								Points midRSp = new Points(((rsPiece.startP.x + rsPiece.endP.x)/2.0f),((rsPiece.startP.y + rsPiece.endP.y)/2.0f));
								//putMarkers(midRSp, 2);
							}
						}							
					}
					else
					{
						boolean bRSSInWS = checkPointInBetween(rs.startP, ws.startP, ws.endP, tolr);
						boolean bRSEInWS = checkPointInBetween(rs.endP, ws.startP, ws.endP, tolr);

						if(bRSSInWS && bRSEInWS)
						{
							WallSegement newWS = new WallSegement(rs.startP, rs.endP, lenRS);
							validRSWallList.add(newWS);

							if(bShowMarkerInter)
							{
								// Marker
								Points midWS = new Points(((newWS.startP.x + newWS.endP.x)/2.0f),((newWS.startP.y + newWS.endP.y)/2.0f));
								//putMarkers(midWS, 1);
							}
						}
						else if(!bRSSInWS)
						{
							boolean bWSSInRS = checkPointInBetween(ws.startP, rs.startP, rs.endP, tolr);

							if(bWSSInRS)
							{
								float len = calcDistance(ws.startP, rs.endP);

								WallSegement newWS = new WallSegement(ws.startP, rs.endP, len);
								validRSWallList.add(newWS);

								if(bShowMarkerInter)
								{
									// Marker
									//Points midWS = new Points(((newWS.startP.x + newWS.endP.x)/2.0f),((newWS.startP.y + newWS.endP.y)/2.0f));
									//putMarkers(midWS, 3);
								}
							}
							else
							{
								float len = calcDistance(ws.endP, rs.endP);

								WallSegement newWS = new WallSegement(ws.endP, rs.endP, len);
								validRSWallList.add(newWS);

								if(bShowMarkerInter)
								{
									// Marker
									//Points midWS = new Points(((newWS.startP.x + newWS.endP.x)/2.0f),((newWS.startP.y + newWS.endP.y)/2.0f));
									//putMarkers(midWS, 3);
								}
							}
						}
						else if(!bRSEInWS)
						{
							boolean bWSSInRS = checkPointInBetween(ws.startP, rs.startP, rs.endP, tolr);

							if(bWSSInRS)
							{
								float len = calcDistance(ws.startP, rs.startP);

								WallSegement newWS = new WallSegement(ws.startP, rs.startP, len);
								validRSWallList.add(newWS);

								if(bShowMarkerInter)
								{
									// Marker
									//Points midWS = new Points(((newWS.startP.x + newWS.endP.x)/2.0f),((newWS.startP.y + newWS.endP.y)/2.0f));
									//putMarkers(midWS, 3);
								}
							}
							else
							{
								float len = calcDistance(ws.endP, rs.startP);

								WallSegement newWS = new WallSegement(ws.endP, rs.startP, len);
								validRSWallList.add(newWS);

								if(bShowMarkerInter)
								{
									// Marker
									//Points midWS = new Points(((newWS.startP.x + newWS.endP.x)/2.0f),((newWS.startP.y + newWS.endP.y)/2.0f));
									//putMarkers(midWS, 3);
								}
							}
						}

					}
				}
			}


			if(bShowMarker)
			{			
				for(WallSegement validWS : validRSWallList)
				{
					// Marker
					Points midWS = new Points(((validWS.startP.x + validWS.endP.x)/2.0f),((validWS.startP.y + validWS.endP.y)/2.0f));
					//putMarkers(midWS, 6);
				}
			}

			return validRSWallList;
		}

		public List<WallSegement> getInnerWalls()
		{
			//String wsStr = "";
			List<WallSegement> wallSegList = new ArrayList<WallSegement>();

			for(int w = 0; w < wallIds.size(); w++)
			{
				List<Points> validPoints = new ArrayList<Points>();

				for(int ws = 0; ws < wallRects.get(w).length; ws++)
				{
					Points p = new Points(wallRects.get(w)[ws][0], wallRects.get(w)[ws][1]);

					if(room.containsPoint(p.x, p.y, (ROOM_TOLERANCE * WALL_THICKNESS)))
						validPoints.add(p);
				}

				for(int i = 1; i < validPoints.size(); i++)
				{
					LineSegement ls = new LineSegement( (validPoints.get(i-1)), (validPoints.get(i)) );					

					float dist = calcDistance(ls.startP, ls.endP);
					wallSegList.add(new WallSegement(ls.startP, ls.endP, dist));

					//putMarkers(ls.startP, 6);
					//putMarkers(ls.endP, 5);
					//wsStr += (wallIds.get(w) + " : (" + ls.startP.x + "," + ls.startP.y + ") -> (" + ls.endP.x + "," + ls.endP.y + ")\n");			
				}

				//wsStr += ("------------------\n\n");
			}

			//JOptionPane.showMessageDialog(null, wsStr);

			return wallSegList;
		}

		public List<WallSegement> calcFreeWallIntersectionsBelowElev(List<WallSegement> validWSList, float elv, Room r, float tolr)
		{
			List<WallSegement> freeWallSegList = new ArrayList<WallSegement>();

			// Compare which furn obj have elevation less than "elv"
			// Take intersection points for objects whose elevation < "elv"

			try
			{
				for(WallSegement ws : validWSList)
				{
					TreeMap<Float, Intersect> interMap = new TreeMap<Float, Intersect>();

					Intersect wallS = new Intersect(ws.startP, 0.0f);
					interMap.put(0.0f, wallS);

					Intersect wallE = new Intersect(ws.endP, ws.len);
					interMap.put(ws.len, wallE);

					for(int f = 0; f < furnElevs.size(); f++)
					{						
						float furnElev = furnElevs.get(f);

						if(elv >= furnElev)
						{							
							LineSegement ref = new LineSegement(ws.startP, ws.endP);	

							List<Intersect> interList = new ArrayList<Intersect>();

							String furnId = furnIds.get(f).toLowerCase();

							// Ignore Windows, Balcony doors and Wall openings while calculating FWS
							if(!fwsExpIds.contains(furnId))
								interList = checkIntersect(ref, furnIds.get(f));
							//else
							//JOptionPane.showMessageDialog(null, furnId);

							int interCount = 0;

							for(Intersect inter : interList)
							{
								//if(r.containsPoint(inter.p.x, inter.p.y, tolr))
								if(checkPointInBetween(inter.p, ws.startP, ws.endP, tolr))
								{			
									interCount++;

									interMap.put(inter.dist, inter);

									if(bShowMarkerInter)
										putMarkers(inter.p, 3);
								}
							}

							if(interCount == 1)
							{
								float X = furnList.get(f).getX();
								float Y = furnList.get(f).getY();

								Points midP = new Points(X, Y);

								float calcDS = calcDistance(midP, ws.startP);
								float calcDE = calcDistance(midP, ws.endP);

								Intersect inter;

								if(calcDS <= calcDE)
								{
									inter = new Intersect(ws.startP, 0.5f);
									interMap.put(inter.dist, inter);

									//if(bShowMarkerInter)
									//putMarkers(inter.p, 4);
								}
								else
								{
									inter = new Intersect(ws.endP, (ws.len - 0.5f));
									interMap.put(inter.dist, inter);

									//if(bShowMarkerInter)
									//putMarkers(inter.p, 4);
								}
							}
							else if(interCount == 0)
							{
								Intersect inter = new Intersect(ws.endP, (ws.len - 0.5f));
								interMap.put(inter.dist, inter);

								//if(bShowMarkerInter)
								//putMarkers(inter.p, 5);
							}
						}
					}

					// Truncate the map so that end point is ws.endP	
					NavigableMap<Float, Intersect> interSet = interMap.headMap(ws.len, true);

					Set<Float> keys = interSet.keySet();
					List<Intersect> inList = new ArrayList<Intersect>();

					for(Float k : keys)
					{
						inList.add(interSet.get(k));
					}					

					for(int k = 1; k < inList.size();)
					{
						Intersect inter1 = inList.get(k - 1);
						Intersect inter2 = inList.get(k);

						WallSegement fws = new WallSegement(inter1.p, inter2.p, (inter2.dist - inter1.dist));

						freeWallSegList.add(fws);

						//if(bShowMarkerInter)
						{
							// Debug
							Points midFWS = new Points(((fws.startP.x + fws.endP.x)/2.0f),((fws.startP.y + fws.endP.y)/2.0f));
							//putMarkers(midFWS, 2);

							//putMarkers(fws.startP, 1);
							//putMarkers(fws.endP, 2);
						}

						k+= 2;
					}
				}
			}
			catch(Exception e) 
			{
				JOptionPane.showMessageDialog(null," -x-x-x- EXCEPTION [calcFreeWallIntersectionsBelowElev]: " + e.getMessage()); 
				e.printStackTrace();
			}

			return freeWallSegList;
		}

		// ======================= INIT FUNCTIONS ======================= //

		public void getLivingRoom()
		{			
			for(Room r : home.getRooms())
			{			
				String roomName = r.getName();

				if((roomName != null) && (roomName.equalsIgnoreCase("living")))
				{
					livingRoom = r;
					break;
				}
			}
		}

		public void init()
		{
			furnIds = new ArrayList<String>();
			furnRects = new ArrayList<float[][]>();
			furnRectsBloated = new ArrayList<float[][]>();

			wallIds = new ArrayList<String>();
			wallRects = new ArrayList<float[][]>();

			fwsExpIds = new ArrayList<String>();

			accBoxName.add("door");
			accBoxName.add("balcony door");
			accBoxName.add("window");
			accBoxName.add("wall opening");
		}		

		public void storeAllFurnParams(Home h)
		{	
			//String dbgStr = "";

			for(HomePieceOfFurniture hp: h.getFurniture())
			{
				String fName = hp.getName();

				if(!markBoxName.contains(fName))
				{			
					//dbgStr += hp.getName() + "\n";

					furnIds.add(fName);
					furnRects.add(hp.getPoints());
					furnRectsAccess.add(hp.getPoints());
					furnElevs.add(hp.getElevation());
					//furnHeights.add(hp.getHeight());
					furnList.add(hp);

					HomePieceOfFurniture hClone = hp.clone();
					float d = hp.getDepth();
					float w = hp.getWidth();

					hClone.setDepth(d + FURNITURE_BLOAT_SIZE);
					hClone.setWidth(w + FURNITURE_BLOAT_SIZE);
					hClone.setElevation(0.0f);

					furnRectsBloated.add(hClone.getPoints());

					createAccBox(hp, fName.toLowerCase());
				}
			}

			//JOptionPane.showMessageDialog(null, dbgStr);
		}

		public void storeFurnParams(HomePieceOfFurniture hpf)
		{			
			String fName = hpf.getName();

			if(!markBoxName.contains(fName) )
			{
				//JOptionPane.showMessageDialog(null, fName);

				furnIds.add(fName);
				furnRects.add(hpf.getPoints());
				furnRectsAccess.add(hpf.getPoints());
				furnElevs.add(hpf.getElevation());  // ??
				//furnHeights.add(hpf.getHeight());
				furnList.add(hpf);

				HomePieceOfFurniture hClone = hpf.clone();
				float d = hpf.getDepth();
				float w = hpf.getWidth();

				hClone.setDepth(d + FURNITURE_BLOAT_SIZE);
				hClone.setWidth(w + FURNITURE_BLOAT_SIZE);
				hClone.setElevation(0.0f);

				furnRectsBloated.add(hClone.getPoints());
			}
		}

		public void clearFurnParams(HomePieceOfFurniture hpf)
		{			
			String fName = hpf.getName();

			if(!markBoxName.contains(fName) )
			{
				int indx = furnIds.indexOf(hpf.getName());

				//JOptionPane.showMessageDialog(null, fName + " : " + indx);				

				if(indx > -1)
				{
					furnIds.remove(indx);
					furnRects.remove(indx);
					furnRectsBloated.remove(indx);

					furnRectsAccess.remove(indx);
					furnElevs.remove(indx);
					furnList.remove(indx);
					//furnHeights.remove(indx);
				}
			}
		}

		public void storeAllWallRects(Home h)
		{
			int wallCount = 1;

			//String furnRect = "";

			for(Wall w: h.getWalls())
			{
				wallIds.add("wall_" + wallCount);

				float[][] wRect = w.getPoints();
				wallRects.add(wRect);	
				wallLists.add(w);

				w.setThickness(WALL_THICKNESS);
				w.setHeight(WALL_HEIGHT);
				//furnRect = ("Wall_"+ wallCount +" : " + wRect[0][0] + "," + wRect[0][1] + " / " + wRect[1][0] + "," + wRect[1][1] + " / " + wRect[2][0] + "," + wRect[2][1] + " / " + wRect[3][0] + "," + wRect[3][1] + "\n\n");

				wallCount++;
			}

			//JOptionPane.showMessageDialog(null, furnRect);
		}

		public void getAccessBox()
		{
			List<FurnitureCategory> fCatg = getUserPreferences().getFurnitureCatalog().getCategories();

			for(int c = 0; c < fCatg.size(); c++)
			{
				List<CatalogPieceOfFurniture> catPOF = fCatg.get(c).getFurniture();

				for(int p = 0; p < catPOF.size(); p++ )
				{
					if(catPOF.get(p).getName().equals("DoorAccBox"))
					{
						accBox = new HomePieceOfFurniture(catPOF.get(p));
					}
				}	
			}
		}

		// ======================= UTIL FUNCTIONS ======================= //

		public void createAccBox(HomePieceOfFurniture hpf, String fName)
		{
			String accessBoxName = "";

			float dist = (0.5f * ACCESSBOX_DEPTH);

			//JOptionPane.showMessageDialog(null, fName);

			if(fName.contains("door"))
			{
				accessBoxName = "DoorAccBox" + accBoxCount;

				if(fName.equalsIgnoreCase("entry door"))
				{
					dist = (0.5f * ACCESSBOX_DEPTH) - (hpf.getDepth() - WALL_THICKNESS);	// As in SH3D, it protrudes out of the wall and into the room
				}
				else if(fName.contains("balcony door"))
				{
					accessBoxName = "BalcAccBox" + accBoxCount;
					fwsExpIds.add(fName);			// Adding fixture and its acc box in exception list
				}

				fwsExpIds.add(accessBoxName.toLowerCase());
			}
			else if(fName.contains("window"))
			{
				accessBoxName = "WindAccBox" + accBoxCount;
				fwsExpIds.add(fName);			// Adding fixture and its acc box in exception list
				fwsExpIds.add(accessBoxName.toLowerCase());
			}
			else if(fName.contains("wall opening"))
			{
				accessBoxName = "WallOAccBox" + accBoxCount;
				fwsExpIds.add(fName);			// Adding fixture and its acc box in exception list
				fwsExpIds.add(accessBoxName.toLowerCase());
			}
			else
				return;

			HomePieceOfFurniture accBoxFurn = accBox.clone();

			float[][] furnRect = hpf.getPoints();

			List<Points> innerP = new ArrayList<Points>();

			for(int f = 0 ; f < furnRect.length; f++)
			{			
				boolean bInRoom = room.containsPoint(furnRect[f][0], furnRect[f][1], ROOM_TOLERANCE);

				if(bInRoom)
				{
					innerP.add(new Points(furnRect[f][0], furnRect[f][1]));
					//putMarkers(new Points(furnRect[f][0], furnRect[f][1]), 1);
				}
			}

			if(innerP.size() > 1)
			{				
				LineSegement ls = new LineSegement(innerP.get(0), innerP.get(1));

				if(accessBoxName.startsWith("BalcAccBox") || accessBoxName.startsWith("WallOAccBox"))
				{
					accBoxCount++;
					
					float totalW = hpf.getWidth();
					
					int nameCount = 0;

					if(totalW > ACCESSBOX_MIN_WIDTH)
					{
						int incr = new Float(totalW / ACCESSBOX_MIN_WIDTH).intValue();
						//JOptionPane.showMessageDialog(null, "incr : " + incr);
						
						Points startP = ls.startP;

						for(int i = 0; i < incr; i++)
						{
							List<Points> incrPListS = getIntersectionCircleLine(ls.startP, (ACCESSBOX_MIN_WIDTH * (i+1)), ls.startP, ls.endP);

							for (Points pS : incrPListS)
							{
								boolean bInside = checkPointInBetween(pS, startP, ls.endP, tolerance);
								
								if(bInside)
								{	
									LineSegement subLS = new LineSegement(startP, pS);
									//putMarkers(pS, 1);
									startP = pS;
									
									List<Points> snapPList = calcSnapCoordinate(subLS, subLS, dist, room, tolerance);
	
									if(snapPList.size() > 0)
									{
										nameCount++;
										Points p = snapPList.get(0);
										//putMarkers(p, 3);
									
										//JOptionPane.showMessageDialog(null, "nameCount : " + nameCount);
										
										HomePieceOfFurniture accBoxFurnSub = accBoxFurn.clone();
										
										accBoxFurnSub.setName(accessBoxName + "_" + nameCount);
										accBoxFurnSub.setX(p.x);
										accBoxFurnSub.setY(p.y);
										accBoxFurnSub.setDepth(ACCESSBOX_DEPTH);
										accBoxFurnSub.setWidth(ACCESSBOX_MIN_WIDTH);
										accBoxFurnSub.setAngle(hpf.getAngle());
	
										//home.addPieceOfFurniture(accBoxFurnSub);
										//storeFurnParams(accBoxFurnSub);
									}
									//else
									//JOptionPane.showMessageDialog(null, "No valid co-ords!!!");
								}
							}
						}
						
						startP = ls.endP;

						for(int i = 0; i < incr; i++)
						{
							List<Points> incrPListE = getIntersectionCircleLine(ls.endP, (ACCESSBOX_MIN_WIDTH * (i+1)), ls.startP, ls.endP);

							for (Points pE : incrPListE)
							{
								boolean bInside = checkPointInBetween(pE, ls.startP, startP, tolerance);
								
								if(bInside)
								{	
									LineSegement subLS = new LineSegement(startP, pE);
									//putMarkers(pE, 2);
									startP = pE;
									
									List<Points> snapPList = calcSnapCoordinate(subLS, subLS, dist, room, tolerance);
	
									if(snapPList.size() > 0)
									{
										nameCount++;
										Points p = snapPList.get(0);
										//putMarkers(p, 4);
									
										//JOptionPane.showMessageDialog(null, "nameCount : " + nameCount);
										
										HomePieceOfFurniture accBoxFurnSub = accBoxFurn.clone();
										
										accBoxFurnSub.setName(accessBoxName + "_" + nameCount);
										accBoxFurnSub.setX(p.x);
										accBoxFurnSub.setY(p.y);
										accBoxFurnSub.setDepth(ACCESSBOX_DEPTH);
										accBoxFurnSub.setWidth(ACCESSBOX_MIN_WIDTH);
										accBoxFurnSub.setAngle(hpf.getAngle());
	
										//home.addPieceOfFurniture(accBoxFurnSub);
										//storeFurnParams(accBoxFurnSub);
									}
									//else
									//JOptionPane.showMessageDialog(null, "No valid co-ords!!!");
								}
							}
						}
					}

					List<Points> snapPList = calcSnapCoordinate(ls, ls, dist, room, tolerance);

					if(snapPList.size() > 0)
					{
						accBoxCount++;

						Points p = snapPList.get(0);
						//putMarkers(p, 3);
						
						accBoxFurn.setName(accessBoxName);
						accBoxFurn.setX(p.x);
						accBoxFurn.setY(p.y);
						accBoxFurn.setDepth(ACCESSBOX_DEPTH);
						accBoxFurn.setWidth(hpf.getWidth());
						accBoxFurn.setAngle(hpf.getAngle());

						home.addPieceOfFurniture(accBoxFurn);
						storeFurnParams(accBoxFurn);
					}
					//else
					//JOptionPane.showMessageDialog(null, "No valid co-ords!!!");
				}
				else
				{
					List<Points> snapPList = calcSnapCoordinate(ls, ls, dist, room, tolerance);

					if(snapPList.size() > 0)
					{
						accBoxCount++;

						Points p = snapPList.get(0);
						//putMarkers(p, 3);
						
						accBoxFurn.setName(accessBoxName);
						accBoxFurn.setX(p.x);
						accBoxFurn.setY(p.y);
						accBoxFurn.setDepth(ACCESSBOX_DEPTH);
						accBoxFurn.setWidth(hpf.getWidth());
						accBoxFurn.setAngle(hpf.getAngle());

						home.addPieceOfFurniture(accBoxFurn);
						storeFurnParams(accBoxFurn);
					}
					//else
					//JOptionPane.showMessageDialog(null, "No valid co-ords!!!");
				}

			}			
		}

		public void cleanupMarkers()
		{
			for(HomePieceOfFurniture hpf : home.getFurniture())
			{
				if(markBoxName.contains(hpf.getName()))
					home.deletePieceOfFurniture(hpf);
			}
		}	

		public void cleanupRealFurnAndWall(List<HomePieceOfFurniture> fList, Wall w)
		{					
			for(HomePieceOfFurniture hpf : fList)
			{
				home.deletePieceOfFurniture(hpf);
			}

			if(w != null)
			{
				w.setRightSideColor(orgWallColor);
				w.setRightSideTexture(null);

				w.setLeftSideColor(orgWallColor);
				w.setLeftSideTexture(null);
			}
		}

		public void cleanupExp()
		{
			for(HomePieceOfFurniture hpf : home.getFurniture())
			{
				if(hpf.getName().startsWith("PCSRect"))
					home.deletePieceOfFurniture(hpf);
			}
		}

		public boolean checkPointOnSameSide(Points a, Points b, Points pS1, Points pS2)
		{
			boolean bRet = false;

			// ((y1−y2)(ax−x1)+(x2−x1)(ay−y1))((y1−y2)(bx−x1)+(x2−x1)(by−y1)) < 0

			float res = ( ((pS1.y - pS2.y)*(a.x - pS1.x)) + ((pS2.x - pS1.x)*(a.y - pS1.y)) )*( ((pS1.y - pS2.y)*(b.x - pS1.x)) + ((pS2.x - pS1.x)*(b.y - pS1.y)) );

			if(res < 0)
				bRet = false;
			else
				bRet = true;

			return bRet;
		}

		public List<Points> calcSnapCoordinate(LineSegement ws, LineSegement ls, float dist, Room r, float tolr) 
		{
			List<Points> finalPList = new ArrayList<Points>();
			List<Points> retPList = new ArrayList<Points>();

			Points wsMidP = new Points(((ws.startP.x + ws.endP.x)/2.0f),(ws.startP.y + ws.endP.y)/2.0f);

			Points centerP = new Points(((ls.startP.x + ls.endP.x)/2.0f),(ls.startP.y + ls.endP.y)/2.0f);

			//putMarkers(ws.startP, 5);
			//putMarkers(ws.endP, 5);

			float xLimit = Math.abs(ws.endP.x - ws.startP.x);
			float yLimit = Math.abs(ws.endP.y - ws.startP.y);

			//JOptionPane.showMessageDialog(null, "xLimit:" + xLimit + ", yLimit:" + yLimit + ", tolr:" + tolr);

			if(yLimit < tolr)
			{
				// Perpendicular - towards wall
				if(yLimit < tolr)
				{
					Points p1 = new Points(centerP.x, (centerP.y + dist));
					Points p2 = new Points(centerP.x, (centerP.y - dist));

					//JOptionPane.showMessageDialog(null, "1_ p1 : " + p1.x + ", " + p1.y + ",\np2 : " + p2.x + ", " + p2.y);

					List<Points> interPList2 = new ArrayList<Points>();
					interPList2.add(p1);
					interPList2.add(p2);

					retPList.addAll(interPList2);
				}
				else if(yLimit >= tolr)
				{
					float slopePara = ((ws.endP.y - ws.startP.y) / (ws.endP.x - ws.startP.x));
					float slopePerp = (-1.0f / slopePara);
					float intercept = centerP.y - (slopePerp * centerP.x);

					//JOptionPane.showMessageDialog(null, "1_ slopePara : " + slopePara + ",\nslopePerp : " + slopePerp);

					List<Points> interPList2 = getIntersectionCircleLine2(centerP, dist, slopePerp, intercept);
					List<Points> sortedPList2 = sortPList(interPList2, wsMidP);

					retPList.addAll(sortedPList2);
				}

				//JOptionPane.showMessageDialog(null, slopePerp + "/ interceptPerp : " + intercept);
			}
			else if(xLimit < tolr)
			{
				// Perpendicular - towards wall
				if(xLimit < tolr)
				{
					Points p1 = new Points((centerP.x + dist), centerP.y);
					Points p2 = new Points((centerP.x - dist), centerP.y);

					//JOptionPane.showMessageDialog(null, "2_ p1 : " + p1.x + ", " + p1.y + ",\np2 : " + p2.x + ", " + p2.y);

					List<Points> interPList1 = new ArrayList<Points>();
					interPList1.add(p1);
					interPList1.add(p2);

					retPList.addAll(interPList1);
				}
				else if(xLimit >= tolr)
				{
					float slopePara = ((ws.endP.y - ws.startP.y) / (ws.endP.x - ws.startP.x));
					float slopePerp = (-1.0f / slopePara);
					float intercept = centerP.y - (slopePerp * centerP.x);

					//JOptionPane.showMessageDialog(null, "2_ slopePara : " + slopePara + ",\nslopePerp : " + slopePerp);

					List<Points> interPList1 = getIntersectionCircleLine2(centerP, dist, slopePerp, intercept);				
					List<Points> sortedPList1 = sortPList(interPList1, wsMidP);

					retPList.addAll(sortedPList1);
				}			
				//JOptionPane.showMessageDialog(null, slopePerp + "/ interceptPerp : " + intercept);				
			}
			else
			{
				// Perpendicular - towards longest wall
				if(yLimit < tolr)
				{
					Points p1 = new Points(centerP.x, (centerP.y + dist));
					Points p2 = new Points(centerP.x, (centerP.y - dist));

					//JOptionPane.showMessageDialog(null, "3_ p1 : " + p1.x + ", " + p1.y + ",\np2 : " + p2.x + ", " + p2.y);

					List<Points> interPList2 = new ArrayList<Points>();
					interPList2.add(p1);
					interPList2.add(p2);

					retPList.addAll(interPList2);
				}
				else if(yLimit >= tolr)
				{
					float slopePara = ((ws.endP.y - ws.startP.y) / (ws.endP.x - ws.startP.x));
					float slopePerp = (-1.0f / slopePara);
					float intercept = centerP.y - (slopePerp * centerP.x);

					//JOptionPane.showMessageDialog(null, "3_ slopePara : " + slopePara + ",\nslopePerp : " + slopePerp);

					List<Points> interPList2 = getIntersectionCircleLine2(centerP, dist, slopePerp, intercept);
					List<Points> sortedPList2 = sortPList(interPList2, wsMidP);

					retPList.addAll(sortedPList2);
				}			
				//JOptionPane.showMessageDialog(null, slopePerp + "/ interceptPerp : " + intercept);
			}

			for(Points pt : retPList)
			{
				//putMarkers(pt, 5);

				if(r.containsPoint(pt.x, pt.y, ROOM_TOLERANCE))
					finalPList.add(pt);
			}

			return finalPList;
		}

		public List<Points> getIntersectionCircleLine2(Points center, float rad, float slope, float intercept)
		{
			List<Points> interList = new ArrayList<Points>();

			try
			{	
				// Equation of Line
				float m = slope;
				float c = intercept;

				// (m^2+1)x^2 + 2(mca��mq−p)x + (q^2−r^2+p^2−2cq+c^2) = 0			

				float A = (m*m) + 1;
				float B = 2*((m*c) - (m*center.y) - center.x);
				float C = (center.y*center.y) - (rad*rad) + (center.x*center.x) - 2*(c*center.y) + (c*c);

				float D = (B*B) - 4*A*C;

				if(D == 0)
				{
					float x1 = ((-B) + (float)Math.sqrt(D)) / (2*A);
					float y1 = (m*x1) + c;

					Points inter = new Points(x1, y1);
					interList.add(inter);	

					//putMarkers(inter, true);
				}
				else if (D > 0)
				{
					float x1 = ((-B) + (float)Math.sqrt(D)) / (2*A);
					float y1 = (m*x1) + c;

					Points inter1 = new Points(x1, y1);
					interList.add(inter1);

					//putMarkers(inter1, false);

					float x2 = ((-B) - (float)Math.sqrt(D)) / (2*A);
					float y2 = (m*x2) + c;

					Points inter2 = new Points(x2, y2);
					interList.add(inter2);

					//putMarkers(inter2, false);
				}		
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null," -xxxxx- EXCEPTION : " + e.getMessage()); 
				e.printStackTrace();
			}

			return interList;
		}

		public float calcDistanceParallel(LineSegement ls1, LineSegement ls2, float tolr)
		{
			float xLimit = Math.abs(ls1.endP.x - ls1.startP.x);
			float yLimit = Math.abs(ls1.endP.y - ls1.startP.y);

			float d = 0.0f;

			if(xLimit < tolr)
			{
				d = Math.abs(ls2.endP.x - ls1.endP.x);
			}
			else if(yLimit < tolr)
			{
				d = Math.abs(ls2.endP.y - ls1.endP.y);
			}
			else
			{			
				float M = (ls1.endP.y - ls1.startP.y) / (ls1.endP.x - ls1.startP.x);									// (y2-y1)/(x2-x1)

				float B1 = ((ls1.startP.y * ls1.endP.x) - (ls1.endP.y * ls1.startP.x)) / (ls1.endP.x - ls1.startP.x);	// (y1x2 - y2x1)/(x2-x1)
				float B2 = ((ls2.startP.y * ls2.endP.x) - (ls2.endP.y * ls2.startP.x)) / (ls2.endP.x - ls2.startP.x);

				d = (Math.abs(B2 - B1) / ((float) Math.sqrt((M*M) + 1)));
			}

			return d;
		}

		public HomePieceOfFurniture searchMatchFurn(String furnName)
		{
			HomePieceOfFurniture matchFurn = null;

			try 
			{				
				List<HomePieceOfFurniture> furnList = home.getFurniture();

				for(int p = 0; p < furnList.size(); p++ )
				{
					if(furnName.equalsIgnoreCase(furnList.get(p).getName()))
					{
						matchFurn = furnList.get(p);
						break;
					}
				}			
			}
			catch(Exception e){e.printStackTrace();}

			return matchFurn;
		}

		public List<HomeTexture> searchMatchTexture(String textName)
		{			
			List<HomeTexture> txtList = new ArrayList<HomeTexture>();
			List<TexturesCategory> fCatg = getUserPreferences().getTexturesCatalog().getCategories();

			try 
			{
				for(int c = 0; c < fCatg.size(); c++ )
				{
					List<CatalogTexture> catTxtList = fCatg.get(c).getTextures();

					for(int p = 0; p < catTxtList.size(); p++ )
					{						
						CatalogTexture catT = catTxtList.get(p);

						//JOptionPane.showMessageDialog(null, catT);

						if(catT.getName().equalsIgnoreCase(textName))
						{							
							//JOptionPane.showMessageDialog(null, catT.getName());	
							txtList.add(new HomeTexture(catT));							
						}
					}
				}				
			}
			catch(Exception e){JOptionPane.showMessageDialog(null, e.getMessage()); e.printStackTrace();}

			return txtList;
		}

		public List<HomePieceOfFurniture> searchCatalog(String furnName, float width, float depth)
		{
			List<HomePieceOfFurniture> ctFurnList = new ArrayList<HomePieceOfFurniture>();
			List<String> typeArr = Arrays.asList(seatingTypeArr);

			//String dbgStr = "";

			float w = width * CONV_IN_M;
			float d = depth * CONV_IN_M;

			HomePieceOfFurniture matchFurn = null;
			List<FurnitureCategory> fCatg = getUserPreferences().getFurnitureCatalog().getCategories();		

			try 
			{
				for(int c = 0; c < fCatg.size(); c++ )
				{
					List<CatalogPieceOfFurniture> catPOFList = fCatg.get(c).getFurniture();

					for(int p = 0; p < catPOFList.size(); p++ )
					{
						CatalogPieceOfFurniture catF = catPOFList.get(p);

						if(catF.getName().toLowerCase().contains(furnName.toLowerCase()))
						{
							if(typeArr.contains(catF.getName()))
								continue; 

							//JOptionPane.showMessageDialog(null,catF.getName() + " -> w : " + w + " cm, d : " + d + " cm \n");

							matchFurn = new HomePieceOfFurniture(catF);

							float cW = matchFurn.getWidth();
							float cD = matchFurn.getDepth();

							if(furnName.contains("table"))
							{
								float cH = matchFurn.getHeight();

								if((cW <= w) && (cD <= d) && (cH <= CENTER_TABLE_HEIGHT))
								{
									ctFurnList.add(matchFurn);
									//JOptionPane.showMessageDialog(null,catF.getName() + " -> w : " + cW + " cm, h : " + cH + " cm \n"); 
								}
							}
							else
							{
								if((cW <= w) && (cD <= d))
								{
									ctFurnList.add(matchFurn);
									//dbgStr += catF.getName() + " -> w : " + cW + " cm, d : " + cD + " cm \n"; 
								}
							}

						}
					}	
				}				
			}
			catch(Exception e){e.printStackTrace();}

			//JOptionPane.showMessageDialog(null, dbgStr);
			return ctFurnList;
		}

		public Points calcFurnMids(Points p1, Points p2, float d, Room inRoom)
		{
			Points retPoints = new Points();

			float l = calcDistance(p1,p2);
			float r = (float)Math.sqrt((d*d) + (0.25f*l*l));

			float e = (p2.x - p1.x);
			float f = (p2.y - p1.y);
			float p = (float)Math.sqrt((e*e + f*f));
			float k = (0.5f * p);

			float x1 = p1.x + (e*k/p) + (f/p)*((float)Math.sqrt((r*r - k*k)));
			float y1 = p1.y + (f*k/p) - (e/p)*((float)Math.sqrt((r*r - k*k)));

			float x2 = p1.x + (e*k/p) - (f/p)*((float)Math.sqrt((r*r - k*k)));
			float y2 = p1.y + (f*k/p) + (e/p)*((float)Math.sqrt((r*r - k*k)));

			// Check for in Room
			if(inRoom.containsPoint(x1, y1, 0.0f))
			{
				retPoints = new Points(x1, y1);
			}
			else if(inRoom.containsPoint(x2, y2, 0.0f))
			{
				retPoints = new Points(x2, y2);
			}

			return retPoints;

			/*
			 	Let the centers be: (a,b), (c,d)
				Let the radii be: r, s

				  e = c - a                          [difference in x coordinates]
				  f = d - b                          [difference in y coordinates]
				  p = sqrt(e^2 + f^2)                [distance between centers]
				  k = (p^2 + r^2 - s^2)/(2p)         [distance from center 1 to line joining points of intersection]


				  x = a + ek/p + (f/p)sqrt(r^2 - k^2)
				  y = b + fk/p - (e/p)sqrt(r^2 - k^2)
				OR
				  x = a + ek/p - (f/p)sqrt(r^2 - k^2)
				  y = b + fk/p + (e/p)sqrt(r^2 - k^2)		
			 */
		}

		public float chkFurnOrient(HomePieceOfFurniture furn, WallSegement ws, float tolr)
		{		
			float rotation = 0.0f;

			float[][] fRect = furn.getPoints();

			//String furnRect = ("furn : " + fRect[0][0] + "," + fRect[0][1] + " / " + fRect[1][0] + "," + fRect[1][1] + " / " + fRect[2][0] + "," + fRect[2][1] + " / " + fRect[3][0] + "," + fRect[3][1] + "\n\n");
			//JOptionPane.showMessageDialog(null, furnRect);

			putMarkers(new Points(fRect[0][0], fRect[0][1]), 1);
			
			//Points furnBottMid = new Points(((fRect[2][0] + fRect[3][0]) / 2),  ((fRect[2][1] + fRect[3][1]) / 2));
			
			Points fsStartP = new Points(fRect[2][0], fRect[2][1]);
			Points fsEndP = new Points(fRect[3][0], fRect[3][1]);
			
			LineSegement fs = new LineSegement(fsStartP, fsEndP);
			
			//Points wsMid = new Points(((ws.startP.x + ws.endP.x) / 2),  ((ws.startP.y + ws.endP.y) / 2));

			LineSegement ls = new LineSegement(ws);
			
			//float dist = calcDistance(furnBottMid, wsMid);
			float dist = calcDistanceParallel(fs, ls, tolr);

			//JOptionPane.showMessageDialog(null, "dist : " + dist + ", " + FURN_TOLERANCE);
			
			if(dist > FURN_TOLERANCE)
			{
				float ang = furn.getAngle();

				furn.setAngle(ang + (float)Math.PI);
				rotation = 180.0f;

				//JOptionPane.showMessageDialog(null, "rotated 180");
			}
			//else
				//JOptionPane.showMessageDialog(null, "No rotation !!!");

			return rotation;
		}

		public void placeFurnParallelToWall(LineSegement ws, HomePieceOfFurniture furn, Points furnCoords)
		{
			FurnLoc furnLoc = new FurnLoc();
			float furnAngle = calcWallAngles(ws);

			furnLoc.w = furn.getWidth();
			furnLoc.ang = furnAngle;			
			furnLoc.p = furnCoords;	

			placeFurnItem(furn, furnLoc);
		}

		public void placeFurnItem(HomePieceOfFurniture inFurn, FurnLoc fLoc)
		{
			inFurn.setWidth(fLoc.w);
			inFurn.setAngle(fLoc.ang);
			inFurn.setX(fLoc.p.x);
			inFurn.setY(fLoc.p.y);

			home.addPieceOfFurniture(inFurn);
		}

		public float calcWallAngles(LineSegement ws)
		{
			float retAngle = 0.0f;

			float wsAngle =  (float) Math.atan((Math.abs(ws.endP.y - ws.startP.y)) / (Math.abs(ws.endP.x - ws.startP.x))); 

			Points p = new Points((ws.startP.x - ws.endP.x), (ws.startP.y - ws.endP.y));
			int qIndx = getQuadrantInfo(p);

			if(qIndx == 1)
				retAngle = wsAngle;
			else if(qIndx == 2)
				retAngle = (float)(Math.PI) - wsAngle;
			else if(qIndx == 3)
				retAngle = (float)(Math.PI) + wsAngle;
			else if(qIndx == 4)
				retAngle = (float)(2.0f*Math.PI) - wsAngle;

			//JOptionPane.showMessageDialog(null, "angle : " + wsAngle + " -> "+ (retAngle * 180.0f / (float) Math.PI) + ", " + qIndx);

			return retAngle;
		}

		public int getQuadrantInfo(Points p)
		{
			int qIndx = 0;

			if((p.x >= 0.0f) && (p.y > 0.0f))
				qIndx = 1;
			else if((p.x < 0.0f) && (p.y >= 0.0f))
				qIndx = 2;
			else if((p.x <= 0.0f) && (p.y < 0.0f))
				qIndx = 3;
			else if((p.x > 0.0f) && (p.y <= 0.0f))
				qIndx = 4;

			return qIndx;
		}

		public void chkFurnOrient(HomePieceOfFurniture furn, LineSegement ws)
		{			
			float[][] fRect = furn.getPoints();
			Points furnBottMid = new Points(((fRect[2][0] + fRect[3][0]) / 2),  ((fRect[2][1] + fRect[3][1]) / 2));

			Points wsMid = new Points(((ws.startP.x + ws.endP.x) / 2),  ((ws.startP.y + ws.endP.y) / 2));

			float dist = calcDistance(furnBottMid, wsMid);
			//JOptionPane.showMessageDialog(null, "dist : " + dist);

			if(dist > ORIENTATION_TOLERANCE)
			{
				furn.setAngle((float)Math.PI);
				//JOptionPane.showMessageDialog(null, "180 rotation");
			}
		}

		public boolean checkIntersectWithAllFurns(HomePieceOfFurniture hpf, boolean bAddAccessibility, boolean bIgnoreAccBox)
		{
			boolean bIntersects = false;

			for(int x = 0 ; x < furnIds.size(); x++)
			{
				String fName = furnIds.get(x).toLowerCase();

				if(bIgnoreAccBox)
				{
					if(fwsExpIds.contains(fName) && !fName.startsWith("dooracc"))
					{
						//JOptionPane.showMessageDialog(null, "bIgnoreAccBox");

						bIntersects = false;
						continue;
					}
				}

				if(!hpf.getName().equalsIgnoreCase(furnIds.get(x)))
				{	
					float[][] refFurnRect = furnRects.get(x);

					for(int f = 0; f < refFurnRect.length; f++)
					{
						Points startLine = new Points(refFurnRect[f][0], refFurnRect[f][1]);

						Points endLine = null;

						if(f == (refFurnRect.length - 1))
							endLine = new Points(refFurnRect[0][0], refFurnRect[0][1]);
						else
							endLine = new Points(refFurnRect[f+1][0], refFurnRect[f+1][1]);				

						LineSegement ls = new LineSegement(startLine, endLine);

						// For Accessibility check
						List<Intersect> interList = new ArrayList<Intersect>();

						if(bAddAccessibility)
							interList = checkIntersectAccessibility(ls, hpf.getName());
						else
							interList = checkIntersect(ls, hpf.getName());

						for(Intersect inter : interList)
						{
							if(inter != null)
							{
								bIntersects = checkPointInBetween(inter.p, ls.startP, ls.endP, FURN_TOLERANCE);

								if(bIntersects)
									break;
							}
							//putMarkers(inter.p, 3);
						}
					}

					if(bIntersects)
					{
						//JOptionPane.showMessageDialog(null, "bIntersects : " + bIntersects + " / " + hpf.getName() + " -> " + furnIds.get(x));			
						break;
					}
				}

				if(bIntersects)
					break;
			}

			return bIntersects;
		}

		public boolean checkIntersectWithAllFurnsForAccBox(HomePieceOfFurniture hpf, boolean bAddAccessibility)
		{
			boolean bIntersects = false;

			for(int x = 0 ; x < furnIds.size(); x++)
			{
				String fName = furnIds.get(x).toLowerCase();

				if(fwsExpIds.contains(fName))
				{
					//JOptionPane.showMessageDialog(null, "bIgnoreAccBox");

					bIntersects = false;
					continue;
				}

				if(!hpf.getName().equalsIgnoreCase(furnIds.get(x)))
				{	
					float[][] refFurnRect = furnRects.get(x);

					for(int f = 0; f < refFurnRect.length; f++)
					{
						Points startLine = new Points(refFurnRect[f][0], refFurnRect[f][1]);

						Points endLine = null;

						if(f == (refFurnRect.length - 1))
							endLine = new Points(refFurnRect[0][0], refFurnRect[0][1]);
						else
							endLine = new Points(refFurnRect[f+1][0], refFurnRect[f+1][1]);				

						LineSegement ls = new LineSegement(startLine, endLine);

						// For Accessibility check
						List<Intersect> interList = new ArrayList<Intersect>();

						if(bAddAccessibility)
							interList = checkIntersectAccessibility(ls, hpf.getName());
						else
							interList = checkIntersect(ls, hpf.getName());

						for(Intersect inter : interList)
						{
							if(inter != null)
							{
								bIntersects = checkPointInBetween(inter.p, ls.startP, ls.endP, FURN_TOLERANCE);

								if(bIntersects)
								{
									//JOptionPane.showMessageDialog(null, "checkIntersectWithAllFurnsForAccBox : " + furnIds.get(x));
									break;
								}
							}
							//putMarkers(inter.p, 3);
						}
					}

					if(bIntersects)
					{
						//JOptionPane.showMessageDialog(null, "bIntersects : " + bIntersects + " -> " + furnIds.get(x));			
						break;
					}
				}

				if(bIntersects)
					break;
			}

			return bIntersects;
		}
		
		public boolean checkIntersectWSWithAllFurns(WallSegement ws, boolean bAddAccessibility)
		{
			boolean bIntersects = false;

			for(int x = 0 ; x < furnIds.size(); x++)
			{
				LineSegement ls = new LineSegement(ws.startP, ws.endP);

				// For Accessibility check
				List<Intersect> interList = new ArrayList<Intersect>();

				if(bAddAccessibility)
					interList = checkIntersectAccessibility(ls, furnIds.get(x));
				else
					interList = checkIntersect(ls, furnIds.get(x));

				for(Intersect inter : interList)
				{
					if(inter != null)
					{
						bIntersects = checkPointInBetween(inter.p, ls.startP, ls.endP, FURN_TOLERANCE);

						if(bIntersects)
							break;
					}
					//putMarkers(inter.p, 3);
				}


				if(bIntersects)
					break;


				if(bIntersects)
					break;
			}

			return bIntersects;
		}

		public boolean checkIntersectWithAllWalls(HomePieceOfFurniture hpf, boolean bAddAccessibility)
		{
			boolean bIntersects = false;

			for(int x = 0 ; x < wallIds.size(); x++)
			{				
				float[][] refFurnRect = wallRects.get(x);

				for(int f = 0; f < refFurnRect.length; f++)
				{
					Points startLine = new Points(refFurnRect[f][0], refFurnRect[f][1]);

					Points endLine = null;

					if(f == (refFurnRect.length - 1))
						endLine = new Points(refFurnRect[0][0], refFurnRect[0][1]);
					else
						endLine = new Points(refFurnRect[f+1][0], refFurnRect[f+1][1]);				

					LineSegement ls = new LineSegement(startLine, endLine);

					// For Accessibility check
					List<Intersect> interList = new ArrayList<Intersect>();

					if(bAddAccessibility)
						interList = checkIntersectAccessibility(ls, hpf.getName());
					else
						interList = checkIntersect(ls, hpf.getName());

					for(Intersect inter : interList)
					{
						if(inter != null)
						{
							bIntersects = checkPointInBetween(inter.p, ls.startP, ls.endP, FURN_TOLERANCE);

							if(bIntersects)
								break;
						}
						//putMarkers(inter.p, 6);
					}
				}

				if(bIntersects)
					break;
			}

			return bIntersects;
		}

		public boolean checkInsideRoom(Room inRoom, float[][] fRect, float tolr)
		{
			boolean bLiesInside = false;

			for(int f = 0; f < fRect.length; f++)
			{
				bLiesInside = inRoom.containsPoint(fRect[f][0], fRect[f][1], tolr);

				if(!bLiesInside)
					break;
			}

			//JOptionPane.showMessageDialog(null, bLiesInside);

			return bLiesInside;
		}

		public boolean checkInsideHome(List<WallSegement> inWSList, HomePieceOfFurniture refFurn, float tolr)
		{
			boolean bLiesInside = false;			

			float[][] fRect = refFurn.getPoints();

			for(int f = 0; f < fRect.length; f++)
			{
				bLiesInside = room.containsPoint(fRect[f][0], fRect[f][1], tolr);

				if(!bLiesInside)
					break;
			}

			return bLiesInside;
		}

		public List<Intersect> checkIntersectAccessibility(LineSegement r, String furnId)
		{
			List<Intersect> interList = new ArrayList<Intersect>();

			Intersect inter = null;
			int indx = -1;

			if((indx = furnIds.indexOf(furnId)) > -1)
			{ 				
				float[][] fRect = furnRectsAccess.get(indx);

				if(fRect.length == 2)
				{
					LineSegement l1 = new LineSegement((new Points(fRect[0][0], fRect[0][1])) , (new Points(fRect[1][0], fRect[1][1])));

					inter = getIntersectPoint(r, l1);				
					if(inter.dist < INFINITY)
						interList.add(inter);

					//debug += ("1. " + inter.p.x + "," + inter.p.y + " -> " + inter.dist + "\n");
				}
				else if(fRect.length == 4)
				{
					LineSegement l1 = new LineSegement((new Points(fRect[0][0], fRect[0][1])) , (new Points(fRect[1][0], fRect[1][1])));
					LineSegement l2 = new LineSegement((new Points(fRect[1][0], fRect[1][1])) , (new Points(fRect[2][0], fRect[2][1])));
					LineSegement l3 = new LineSegement((new Points(fRect[2][0], fRect[2][1])) , (new Points(fRect[3][0], fRect[3][1])));
					LineSegement l4 = new LineSegement((new Points(fRect[3][0], fRect[3][1])) , (new Points(fRect[0][0], fRect[0][1])));

					inter = getIntersectPoint(r, l1);				
					if(inter.dist < INFINITY)
						interList.add(inter);

					//debug += ("1. " + inter.p.x + "," + inter.p.y + " -> " + inter.dist + "\n");

					inter = getIntersectPoint(r, l2);				
					if(inter.dist < INFINITY)
						interList.add(inter);

					//debug += ("2. " + inter.p.x + "," + inter.p.y + " -> " + inter.dist + "\n");

					inter = getIntersectPoint(r, l3);
					if(inter.dist < INFINITY)
						interList.add(inter);

					//debug += ("3. " + inter.p.x + "," + inter.p.y + " -> " + inter.dist + "\n");

					inter = getIntersectPoint(r, l4);
					if(inter.dist < INFINITY)
						interList.add(inter);

					//debug += ("4. " + inter.p.x + "," + inter.p.y + " -> " + inter.dist + "\n");
				}
				//JOptionPane.showMessageDialog(null, debug);					
			}

			return interList;
		}

		public List<Intersect> checkIntersect(LineSegement r, String furnId)
		{
			List<Intersect> interList = new ArrayList<Intersect>();

			boolean bStop = false;

			Intersect inter = null;
			int indx = -1;

			if(!bStop && ((indx = furnIds.indexOf(furnId)) > -1))
			{ 				
				float[][] fRect = furnRects.get(indx);
				//float[][] fRect = furnRectsBloated.get(indx);

				if(fRect.length == 2)
				{
					LineSegement l1 = new LineSegement((new Points(fRect[0][0], fRect[0][1])) , (new Points(fRect[1][0], fRect[1][1])));

					inter = getIntersectPoint(r, l1);				
					if(inter.dist < INFINITY)
						interList.add(inter);

					//debug += ("1. " + inter.p.x + "," + inter.p.y + " -> " + inter.dist + "\n");
				}
				else if(fRect.length == 4)
				{
					LineSegement l1 = new LineSegement((new Points(fRect[0][0], fRect[0][1])) , (new Points(fRect[1][0], fRect[1][1])));
					LineSegement l2 = new LineSegement((new Points(fRect[1][0], fRect[1][1])) , (new Points(fRect[2][0], fRect[2][1])));
					LineSegement l3 = new LineSegement((new Points(fRect[2][0], fRect[2][1])) , (new Points(fRect[3][0], fRect[3][1])));
					LineSegement l4 = new LineSegement((new Points(fRect[3][0], fRect[3][1])) , (new Points(fRect[0][0], fRect[0][1])));

					inter = getIntersectPoint(r, l1);				
					if(inter.dist < INFINITY)
						interList.add(inter);

					//debug += ("1. " + inter.p.x + "," + inter.p.y + " -> " + inter.dist + "\n");

					inter = getIntersectPoint(r, l2);				
					if(inter.dist < INFINITY)
						interList.add(inter);

					//debug += ("2. " + inter.p.x + "," + inter.p.y + " -> " + inter.dist + "\n");

					inter = getIntersectPoint(r, l3);
					if(inter.dist < INFINITY)
						interList.add(inter);

					//debug += ("3. " + inter.p.x + "," + inter.p.y + " -> " + inter.dist + "\n");

					inter = getIntersectPoint(r, l4);
					if(inter.dist < INFINITY)
						interList.add(inter);

					//debug += ("4. " + inter.p.x + "," + inter.p.y + " -> " + inter.dist + "\n");
				}
				//JOptionPane.showMessageDialog(null, debug);					
			}

			return interList;
		}

		public Intersect getIntersectPointOfLines(LineSegement ref, LineSegement l)
		{
			Intersect inter = new Intersect((new Points()), 0.0f);

			float A = (ref.endP.y - ref.startP.y);											// (y2 - y1)
			float B = (ref.startP.x - ref.endP.x);											// (x1 - x2)		
			float C = ((ref.endP.y * ref.startP.x) - (ref.startP.y * ref.endP.x));			// (y2x1 - y1x2)

			float P = (l.endP.y - l.startP.y);												// (y2' - y1')
			float Q = (l.startP.x - l.endP.x);												// (x1' - x2')		
			float R = ((l.endP.y * l.startP.x) - (l.startP.y * l.endP.x));					// (y2'x1' - y1'x2')

			float yNum = (P*C - R*A);
			float yDen = (P*B - Q*A);

			float xNum = (Q*C - R*B);
			float xDen = (Q*A - P*B);

			JOptionPane.showMessageDialog(null, A+", "+B+", "+C+"; "+P+", "+Q+","+R);

			if(Math.abs(A) <= SLOPE_TOLERANCE)
			{
				if(Math.abs(P) <= SLOPE_TOLERANCE)
				{
					inter.p = new Points(l.startP.x, ref.startP.y);
				}
				else
				{
					float x = 0.0f - ((R + (Q * ref.startP.y)) / P);
					inter.p = new Points(x, ref.startP.y);
				}
			}
			else if(Math.abs(B) <= SLOPE_TOLERANCE)
			{
				if(Math.abs(Q) <= SLOPE_TOLERANCE)
				{
					inter.p = new Points(ref.startP.x, l.startP.y);
				}
				else
				{
					float y = 0.0f - ((R + (P * ref.startP.x)) / Q);
					inter.p = new Points(ref.startP.x, y);
				}
			}
			else if(Math.abs(P) <= SLOPE_TOLERANCE)
			{
				if(Math.abs(A) <= SLOPE_TOLERANCE)
				{
					inter.p = new Points(ref.startP.x, l.startP.y);
				}
				else
				{
					float x = 0.0f - ((C + (B * l.startP.y)) / A);
					inter.p = new Points(x, l.startP.y);
				}
			}
			else if(Math.abs(Q) <= SLOPE_TOLERANCE)
			{
				if(Math.abs(B) <= SLOPE_TOLERANCE)
				{
					inter.p = new Points(l.startP.x, ref.startP.y);
				}
				else
				{
					float y = 0.0f - ((C + (A * l.startP.x)) / B);
					inter.p = new Points(l.startP.x, y);
				}
			}
			else if((Math.abs(xDen) <= SLOPE_TOLERANCE) || (Math.abs(yDen) <= SLOPE_TOLERANCE))
			{
				inter.p = new Points(2*INFINITY, 2*INFINITY);
				inter.dist = INFINITY;
			}
			else
			{
				inter.p = new Points((xNum/xDen), (yNum/yDen));				
				inter.dist = calcDistance(inter.p, ref.startP);
			}

			return inter;			
		}

		public Intersect getIntersectPoint(LineSegement ref, LineSegement l)
		{
			Intersect inter = new Intersect((new Points()), 0.0f);

			float A = (ref.endP.y - ref.startP.y);											// (y2 - y1)
			float B = (ref.startP.x - ref.endP.x);											// (x1 - x2)		
			float C = ((ref.endP.y * ref.startP.x) - (ref.startP.y * ref.endP.x));			// (y2x1 - y1x2)

			float P = (l.endP.y - l.startP.y);												// (y2' - y1')
			float Q = (l.startP.x - l.endP.x);												// (x1' - x2')		
			float R = ((l.endP.y * l.startP.x) - (l.startP.y * l.endP.x));					// (y2'x1' - y1'x2')

			float yNum = (P*C - R*A);
			float yDen = (P*B - Q*A);

			float xNum = (Q*C - R*B);
			float xDen = (Q*A - P*B);

			if((xDen == 0.0f) || (yDen == 0.0f))
			{
				inter.p = new Points(2*INFINITY, 2*INFINITY);
				inter.dist = INFINITY;
			}
			else
			{
				inter.p = new Points((xNum/xDen), (yNum/yDen));				
				boolean bC1 = checkPointInBetween(inter.p, l.startP, l.endP, FURN_TOLERANCE);

				//JOptionPane.showMessageDialog(null, bC1 + " /  Intersection -> X : " + inter.p.x + ", Y : " + inter.p.y);

				if(bC1)
				{		
					inter.dist = calcDistance(inter.p, ref.startP);					
				}
				else
				{
					inter.p = new Points(INFINITY, INFINITY);
					inter.dist = INFINITY;
				}
			}

			return inter;			
		}

		public List<Points> getIntersectionCircleLine(Points center, float rad, Points startL, Points endL)
		{
			List<Points> interList = new ArrayList<Points>();

			try
			{	
				if(Math.abs(endL.x - startL.x) < tolerance)
				{
					float dist = (float) Math.abs(startL.x - center.x);

					if(dist <= rad)
					{
						float x01 = startL.x;
						float y01 = center.y - (float)Math.sqrt((rad*rad) - (dist*dist));

						Points inter1 = new Points(x01, y01);
						interList.add(inter1);
						//putMarkers(inter1, false);

						float x02 = startL.x;
						float y02 = center.y + (float)Math.sqrt((rad*rad) - (dist*dist));

						Points inter2 = new Points(x02, y02);
						interList.add(inter2);
						//putMarkers(inter2, false);
					}
					//else : Line does not intersect with this circle
				}
				else
				{
					// Equation of Line
					float m = ((endL.y - startL.y) / (endL.x - startL.x));
					float c = startL.y - (m*startL.x);

					// (m^2+1)x^2 + 2(mc−mq−p)x + (q^2−r^2+p^2−2cq+c^2) = 0			

					float A = (m*m) + 1;
					float B = 2*((m*c) - (m*center.y) - center.x);
					float C = (center.y*center.y) - (rad*rad) + (center.x*center.x) - 2*(c*center.y) + (c*c);

					float D = (B*B) - 4*A*C;

					if(D == 0)
					{
						float x1 = ((-B) + (float)Math.sqrt(D)) / (2*A);
						float y1 = (m*x1) + c;

						Points inter = new Points(x1, y1);
						interList.add(inter);	

						//putMarkers(inter, true);
					}
					else if (D > 0)
					{
						float x1 = ((-B) + (float)Math.sqrt(D)) / (2*A);
						float y1 = (m*x1) + c;

						Points inter1 = new Points(x1, y1);
						interList.add(inter1);

						//putMarkers(inter1, false);

						float x2 = ((-B) - (float)Math.sqrt(D)) / (2*A);
						float y2 = (m*x2) + c;

						Points inter2 = new Points(x2, y2);
						interList.add(inter2);

						//putMarkers(inter2, false);
					}
				}				
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null," -xxxxx- EXCEPTION : " + e.getMessage()); 
				e.printStackTrace();
			}

			return interList;
		}

		public float calcDistance(Points p1, Points p2)
		{
			float d = (float) Math.sqrt(((p2.x - p1.x) * (p2.x - p1.x)) + ((p2.y - p1.y) * (p2.y - p1.y)));
			return d;
		}	

		public boolean isParallel(LineSegement ls1, LineSegement ls2, float tolr)
		{
			boolean isPara = false;

			float slope1 = 0.0f;
			float slope2 = 0.0f;

			if(Math.abs(ls1.endP.x - ls1.startP.x) <= tolr)
				slope1 = INFINITY;
			else
				slope1 = ((ls1.endP.y - ls1.startP.y) / (ls1.endP.x - ls1.startP.x));

			if(Math.abs(ls2.endP.x - ls2.startP.x) <= tolr)
				slope2 = INFINITY;
			else
				slope2 = ((ls2.endP.y - ls2.startP.y) / (ls2.endP.x - ls2.startP.x));

			// Rounding off slope values as per max "tan()" value
			slope1 = (slope1 >= TAN_MAX_VAL) ? INFINITY : slope1;
			slope2 = (slope2 >= TAN_MAX_VAL) ? INFINITY : slope2;
			
			//JOptionPane.showMessageDialog(null, Math.abs(ls1.endP.x - ls1.startP.x) + ", " + Math.abs(ls2.endP.x - ls2.startP.x));
			
			isPara = (Math.abs(slope1 - slope2) < SLOPE_TOLERANCE) ? true : false;

			return isPara;
		}

		public boolean isParallelToFace(LineSegement ls1, LineSegement ls2, float tolr)
		{
			boolean isPara = false;

			float slope1 = -100.0f;
			float slope2 = -100.0f;

			if(Math.abs(ls1.endP.x - ls1.startP.x) <= tolr)
				slope1 = INFINITY;
			else
				slope1 = Math.abs((ls1.endP.y - ls1.startP.y) / (ls1.endP.x - ls1.startP.x));

			if(Math.abs(ls2.endP.x - ls2.startP.x) <= tolr)
				slope2 = INFINITY;
			else
				slope2 = Math.abs((ls2.endP.y - ls2.startP.y) / (ls2.endP.x - ls2.startP.x));

			//JOptionPane.showMessageDialog(null, Math.abs(ls1.endP.x - ls1.startP.x) + ", " + Math.abs(ls2.endP.x - ls2.startP.x));
			
			// Rounding off slope values as per max "tan()" value
			slope1 = (slope1 >= TAN_MAX_VAL) ? INFINITY : slope1;
			slope2 = (slope2 >= TAN_MAX_VAL) ? INFINITY : slope2;
			
			Points ls1MidP = new Points(((ls1.startP.x + ls1.endP.x)/2),(ls1.startP.y + ls1.endP.y)/2);
			//putMarkers(ls1MidP, 0);
			
			Points ls2MidP = new Points(((ls2.startP.x + ls2.endP.x)/2),(ls2.startP.y + ls2.endP.y)/2);
			//putMarkers(ls2MidP, 4);
			
			//JOptionPane.showMessageDialog(null, slope1 + ", " + slope2);
			
			isPara = (Math.abs(slope1 - slope2) < SLOPE_TOLERANCE) ? true : false;

			return isPara;
		}
		
		public float calcDistancePointLine(Points p, LineSegement ls, float tolr)
		{
			float dist = 0.0f;

			if(Math.abs(ls.endP.x - ls.startP.x) < tolr)
			{
				dist = Math.abs(ls.endP.x - p.x);
			}
			else if(Math.abs(ls.endP.y - ls.startP.y) < tolr)
			{
				dist = Math.abs(ls.endP.y - p.y);
			}
			else
			{
				float slope = ((ls.endP.y - ls.startP.y) / (ls.endP.x - ls.startP.x));

				float A = slope;
				float B = -1.0f;
				float C = (ls.startP.y - (slope * ls.startP.x));

				dist = ( Math.abs((A*p.x) + (B*p.y) + C) / ((float)Math.sqrt((A*A) + (B*B))) );
			}

			return dist;
		}	

		public boolean checkPointInBetween(Points test, Points start, Points end, float tolPercent)
		{
			boolean bRet = false;

			float distST = calcDistance(start, test);
			float distTE = calcDistance(test, end);
			float distSE = calcDistance(start, end);

			float distSEAbs = (float)(Math.abs(distST + distTE - distSE));

			if(distSEAbs <= tolPercent)
				bRet = true;

			return bRet;			
		}

		public List<Points> sortPList(List<Points> interPList, Points ref)
		{
			List<Points> retPList = new ArrayList<Points>();
			TreeMap<Float, Points> pMap = new TreeMap<Float, Points>();

			for(Points p : interPList)
			{
				float dist = calcDistance(p, ref);
				pMap.put(dist, p);
			}

			Set<Float> keys = pMap.keySet();

			for(Float d : keys)
			{
				retPList.add(pMap.get(d));
			}

			return retPList;
		}

		public HomePieceOfFurniture getFurnItem(String furnName)
		{
			HomePieceOfFurniture matchFurn = null;
			List<FurnitureCategory> fCatg = getUserPreferences().getFurnitureCatalog().getCategories();		

			try 
			{
				for(int c = 0; c < fCatg.size(); c++ )
				{
					List<CatalogPieceOfFurniture> catPOF = fCatg.get(c).getFurniture();

					for(int p = 0; p < catPOF.size(); p++ )
					{
						if(furnName.equalsIgnoreCase(catPOF.get(p).getName()))
						{
							matchFurn = new HomePieceOfFurniture(catPOF.get(p));
							//JOptionPane.showMessageDialog(null, "Found " + furnName);
							break;
						}
					}	
				}				
			}
			catch(Exception e){e.printStackTrace();}

			return matchFurn;
		}

		// ======================= DEBUG FUNCTIONS ======================= //

		public void putMarkers(Points p, int indx)
		{
			HomePieceOfFurniture box = null;

			box = markBoxes[indx].clone();			
			box.setX(p.x);
			box.setY(p.y);
			home.addPieceOfFurniture(box);
		}

		public void putMarkerLine(LineSegement ls, int indx)
		{
			HomePieceOfFurniture box = null;

			box = markBoxes[indx].clone();			
			box.setX((ls.startP.x + ls.endP.x)/2.0f);
			box.setY((ls.startP.y + ls.endP.y)/2.0f);

			box.setWidth(calcDistance(ls.startP, ls.endP));
			box.setAngle(calcWallAngles(ls));

			box.setName(box.getName().replaceAll("box", "line"));

			home.addPieceOfFurniture(box);
		}

		public HomePieceOfFurniture[] getMarkerBoxes()
		{
			HomePieceOfFurniture[] markBoxes = new HomePieceOfFurniture[MARKBOX_COUNT];
			int count = 0;

			List<FurnitureCategory> fCatg = getUserPreferences().getFurnitureCatalog().getCategories();

			for(int c = 0; c < fCatg.size(); c++ )
			{
				if(count >= MARKBOX_COUNT)
					break;

				List<CatalogPieceOfFurniture> catPOF = fCatg.get(c).getFurniture();

				for(int p = 0; p < catPOF.size(); p++ )
				{
					if(catPOF.get(p).getName().equals("boxred"))
					{
						markBoxes[0] = new HomePieceOfFurniture(catPOF.get(p));
						markBoxName.add("boxred");
						count++;
					}
					else if(catPOF.get(p).getName().equals("boxgreen"))
					{
						markBoxes[1] = new HomePieceOfFurniture(catPOF.get(p));
						markBoxName.add("boxgreen");
						count++;
					}
					else if(catPOF.get(p).getName().equals("boxblue"))
					{
						markBoxes[2] = new HomePieceOfFurniture(catPOF.get(p));
						markBoxName.add("boxblue");
						count++;
					}
					else if(catPOF.get(p).getName().equals("boxyellow"))
					{
						markBoxes[3] = new HomePieceOfFurniture(catPOF.get(p));
						markBoxName.add("boxyellow");
						count++;
					}
					else if(catPOF.get(p).getName().equals("boxteal"))
					{
						markBoxes[4] = new HomePieceOfFurniture(catPOF.get(p));
						markBoxName.add("boxteal");
						count++;
					}
					else if(catPOF.get(p).getName().equals("boxblack"))
					{
						markBoxes[5] = new HomePieceOfFurniture(catPOF.get(p));
						markBoxName.add("boxblack");
						count++;
					}
					else if(catPOF.get(p).getName().equals("boxpurp"))
					{
						markBoxes[6] = new HomePieceOfFurniture(catPOF.get(p));
						markBoxName.add("boxpurp");
						count++;
					}
					else if(catPOF.get(p).getName().equals("boxgray"))
					{
						markBoxes[7] = new HomePieceOfFurniture(catPOF.get(p));
						markBoxName.add("boxgray");
						count++;
					}

					if(count >= MARKBOX_COUNT)
						break;
				}	
			}

			return markBoxes;
		}
	}



	@Override
	public PluginAction[] getActions() 
	{
		return new PluginAction [] {new RoomTestAction()}; 
	}
}
