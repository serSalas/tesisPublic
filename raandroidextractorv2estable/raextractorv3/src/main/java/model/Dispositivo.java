package main.java.model;

/**
 * Esta clase, contiene propiedades asociadas 
 * de un conjunto de dispositivos Android soportados.  
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class Dispositivo
{
	private String fabricante;
	private String modelo;
	private String versionSo;
	private String numeroCompilacion;
	private String idFabricante;
	
	private String rutaParticion;
	private String rutaRecoveryFabrica;
	private String rutaRecoveryPersonalizado;
	private String rutaDirtyCowDcow;
	private String rutaDirtyCowRunas;
	private String rutaDirtyCowModificado;
	private String rutaLglaf;
	private String rutaLglafPartitions;
	private String rutaLglafExtractPartitions;
	private String rutaMtp;
	private String rutaSdcard0;
	private String rutaSdcard1;
	private String cantidadBloques;
	
	private long tamanioParticion;
	private long tamanioLogico;
	
	private boolean seleccionado;
	private boolean metodoDirtyCowModificado;
	private boolean metodoDirtyCow;
	private boolean metodoRecovery;
	private boolean metodoLgLaf;
	private boolean metodoMtp;
	private boolean metodoAdb;
	private boolean metodoUnlockScreenSamsung;
	private boolean metodoDiscoverPattern;
	private boolean metodoDeletePass;
	private boolean metodoAT;
	/**
	 * Este constructor, inicializa atributos dependiendo del tipo de dispositivo Android.
	 * @param fabricante Variable String que contiene marca de dispositivo Android.
	 * @param modelo Variable String que contiene modelo de dispositivo Android.
	 * @param versionSo Variable String que contiene versión de sistema operativo Android.
	 * @param numeroCompilacion Variable String que contiene número de compilación de dispositivo Android.
	 */
	public Dispositivo(String fabricante, String modelo, String versionSo, String numeroCompilacion)
	{
		this.fabricante = fabricante;
		this.modelo = modelo;
		this.versionSo = versionSo;
		this.numeroCompilacion = numeroCompilacion;
		this.seleccionado = false;
		
		switch(fabricante)
		{
//##################################################################################################################################################		
			case "samsung":
			{
				idFabricante = "1256";
				rutaMtp = "";
				rutaLglaf = "";
				rutaLglafExtractPartitions = "";
				rutaLglafPartitions = "";
				cantidadBloques = "";
				tamanioLogico = 0;
				switch(numeroCompilacion)
				{
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "JZO54K.I8550LUBUANF1":						//1- Galaxy win
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = true;
							metodoRecovery = true;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = true;
							metodoDiscoverPattern = true;
							metodoDeletePass = true;
							metodoAT = false;
							
							rutaRecoveryFabrica = "src/main/resources/devices/recovery/samsung/win/recoveryFabrica.img";
							rutaRecoveryPersonalizado = "src/main/resources/devices/recovery/samsung/win/recoveryPersonalizado.img";
							rutaDirtyCowDcow = "src/main/resources/devices/dirtycow/samsung/win/dirtycow";				
							rutaDirtyCowRunas = "src/main/resources/devices/dirtycow/samsung/win/run-as";
							
							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/extSdCard";
							rutaParticion = "/dev/block/mmcblk0p24";
							tamanioParticion = 5573804032L; //5443168 blocks × 1024 bytes
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "JZO54K.I8260LUBAMG3":							//2- Galaxy core
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = true;
							metodoRecovery = true;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = true;
							metodoDiscoverPattern = true;
							metodoDeletePass = true;
							metodoAT = false;
							
							rutaRecoveryFabrica = "src/main/resources/devices/recovery/samsung/core/recoveryFabrica.img";
							rutaRecoveryPersonalizado = "src/main/resources/devices/recovery/samsung/core/recoveryPersonalizado.img";
							rutaDirtyCowDcow = "src/main/resources/devices/dirtycow/samsung/core/dirtycow";
							rutaDirtyCowRunas = "src/main/resources/devices/dirtycow/samsung/core/run-as";
							
							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/extSdCard";
							rutaParticion = "/dev/block/mmcblk0p24";
							tamanioParticion = 5238259712L;
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "KOT49H.G355MUBS0AQA7":						//3- Galaxy core2
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = false;
							metodoRecovery = true;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = true;
							metodoDiscoverPattern = true;
							metodoDeletePass = true;
							metodoAT = false;
							
							rutaRecoveryFabrica= 		"src/main/resources/devices/recovery/samsung/core2/recoveryFabrica.img";
							rutaRecoveryPersonalizado= 	"src/main/resources/devices/recovery/samsung/core2/recoveryPersonalizado.img";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";
							
							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/extSdCard";
							rutaParticion = "/dev/block/mmcblk0p22";
							tamanioParticion = 2415919104L;
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "KOT49H.G130MUBS0AQA1":						//4- Galaxy young2
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = false;
							metodoRecovery = true;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = true;
							metodoDiscoverPattern = true;
							metodoDeletePass = true;
							metodoAT = false;
							
							rutaRecoveryFabrica= "src/main/resources/devices/recovery/samsung/young2/recoveryFabrica.img";
							rutaRecoveryPersonalizado= "src/main/resources/devices/recovery/samsung/young2/recoveryPersonalizado.img";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";
							
							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/extSdCard";
							rutaParticion = "/dev/block/mmcblk0p20";
							tamanioParticion = 2466250752L;
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "MMB29Q.J106MUBS0ARE3":						//5- Galaxy j1miniprime
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = false;
							metodoRecovery = false;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = false;
							metodoDiscoverPattern = false;
							metodoDeletePass = false;
							metodoAT = false;
							
							rutaRecoveryFabrica="";
							rutaRecoveryPersonalizado="";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";
														
							rutaSdcard0 = "/storage/self/primary";
							rutaSdcard1 = "/storage/025B-FE26";
							rutaParticion = "/dev/block/mmcblk0p28";
							tamanioParticion = 4504682496L;
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "MMB29T.G532MUMU1ASH2":						//6- Galaxy j2prime
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = false;
							metodoRecovery = true;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = true;
							metodoDiscoverPattern = true;
							metodoDeletePass = true;
							metodoAT = false;
							
							rutaRecoveryFabrica="src/main/resources/devices/recovery/samsung/j2prime/recoveryFabrica.img";
							rutaRecoveryPersonalizado="src/main/resources/devices/recovery/samsung/j2prime/recoveryPersonalizado.img";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";
														
							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/447D-C37E";
							rutaParticion = "/dev/block/mmcblk0p29";
							tamanioParticion = 3833593856L;
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					
					
					case "LMY47V.J320MUBU0ARA2":							//7- Galaxy j3
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = false;
							metodoRecovery = true;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = true;
							metodoDiscoverPattern = true;
							metodoDeletePass = true;
							metodoAT = false;
							
							rutaRecoveryFabrica= "src/main/resources/devices/recovery/samsung/j3/recoveryFabrica.img";
							rutaRecoveryPersonalizado= "src/main/resources/devices/recovery/samsung/j3/recoveryPersonalizado.img";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";
														
							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/extSdCard";
							rutaParticion = "/dev/block/mmcblk0p27";
							tamanioParticion = 5104467968L; //4984832x1024
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "NRD90M.G570MUBU2BRA3":							//8- Galaxy j5prime
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = false;
							metodoRecovery = false;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = false;
							metodoDiscoverPattern = false;
							metodoDeletePass = false;
							metodoAT = false;

							rutaRecoveryFabrica="";
							rutaRecoveryPersonalizado="";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";

							rutaSdcard0 = "/storage/self/primary";
							rutaSdcard1 = "/storage";
							rutaParticion = "/dev/block/mmcblk0p24";
							tamanioParticion = 12075401216L;
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "M1AJQ.G610MUBU6CSJ1":								//9- Galaxy j7prime
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = false;
							metodoRecovery = false;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = false;
							metodoDiscoverPattern = false;
							metodoDeletePass = false;
							metodoAT = false;
							
							rutaRecoveryFabrica="";
							rutaRecoveryPersonalizado="";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";

							rutaSdcard0 = "sdcard";//"/storage/self/primary";
							rutaSdcard1 = "/storage";
							rutaParticion = "/dev/block/mmcblk0p25";
							tamanioParticion = 27577548800L;
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "LMY48B.J700MUBU1AOK1":							//10- Galaxy j7
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = false;
							metodoRecovery = false;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = false;
							metodoDiscoverPattern = false;
							metodoDeletePass = false;
							metodoAT = false;
							
							rutaRecoveryFabrica= "";
							rutaRecoveryPersonalizado= "";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";

							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/extSdCard";
							rutaParticion = "/dev/block/mmcblk0p23";
							tamanioParticion = 12931039232L; //12627968x1024
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "M1AJQ.J710MNUBS4CSF1":							//11- j7 2016
					{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = false;
							metodoRecovery = false;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = false;
							metodoDiscoverPattern = false;
							metodoDeletePass = false;
							metodoAT = false;
							
							rutaRecoveryFabrica= "";
							rutaRecoveryPersonalizado= "";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";			
							
							rutaSdcard0 = "/storage/self/primary";
							//rutaSdcard1 = "/storage/5688-96D9";
							rutaParticion = "/dev/block/mmcblk0p24";
							tamanioParticion = 12067012608L; //12627968x1024
							break;
					}		
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "JDQ39.G3812BVJUANC2":							//12- Galaxy s3slim
					{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = false;
							metodoRecovery = true;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = true;
							metodoDiscoverPattern = true;
							metodoDeletePass = true;
							metodoAT = false;
							
							rutaRecoveryFabrica= "src/main/resources/devices/recovery/samsung/s3slim/recoveryFabrica.img";
							rutaRecoveryPersonalizado= "src/main/resources/devices/recovery/samsung/s3slim/recoveryPersonalizado.img";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";
							
							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/extSdCard";
							rutaParticion = "/dev/block/mmcblk0p16";
							tamanioParticion = 5860491264L; //12627968x1024
							break;
					}	
//--------------------------------------------------------------------------------------------------------------------------------------------------		
				}
				break;
			}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
//##################################################################################################################################################		
			case "LGE":
				{
					rutaRecoveryFabrica = "";
					rutaRecoveryPersonalizado = "";
					rutaLglaf = "src/main/resources/devices/lglaf/lglaf.py";
					rutaLglafPartitions = "src/main/resources/devices/lglaf/partitions.py";
					rutaLglafExtractPartitions = "src/main/resources/devices/lglaf/extract-partitions.py";
					rutaMtp = "";
					tamanioLogico = 0;	
				switch(numeroCompilacion)
					{
//--------------------------------------------------------------------------------------------------------------------------------------------------		
						case "KOT49I":									
						{
							if(modelo.equals("LG-H221AR"))				//1- LG KITE
							{		
								metodoDirtyCowModificado = false;
								metodoDirtyCow = true;
								metodoRecovery = false;
								metodoLgLaf = false;
								metodoMtp = true;
								metodoAdb = true;
								metodoUnlockScreenSamsung = false;
								metodoDiscoverPattern = false;
								metodoDeletePass = false;
								metodoAT = true;
								
								idFabricante = "4100";
								rutaDirtyCowDcow = "src/main/resources/devices/dirtycow/lge/lgkite/dirtycow";
								rutaDirtyCowRunas = "src/main/resources/devices/dirtycow/lge/lgkite/run-as";

								rutaSdcard0 = "/storage/sdcard0";
								rutaSdcard1 = "/storage/external_SD";
								rutaParticion = "/dev/block/mmcblk0p34";
								tamanioParticion = 1659895808L;//USERDATA
								cantidadBloques = "1620992";
								break;

							}
							else			//2- G2 mini
							{
								metodoDirtyCowModificado = false;
								metodoDirtyCow = true;
								metodoRecovery = false;
								metodoLgLaf = true;
								metodoMtp = true;
								metodoAdb = true;
								metodoUnlockScreenSamsung = false;
								metodoDiscoverPattern = false;
								metodoDeletePass = false;
								metodoAT = true;
								
								idFabricante = "4100";
								rutaDirtyCowDcow = "src/main/resources/devices/dirtycow/lge/lg2mini/dirtycow";
								rutaDirtyCowRunas = "src/main/resources/devices/dirtycow/lge/lg2mini/run-as";

								rutaSdcard0 = "/storage/sdcard0";
								rutaSdcard1 = "/storage/external_SD";
								rutaParticion = "/dev/block/mmcblk0p20";
								tamanioParticion = 4812963840L;//USERDATA
								cantidadBloques = "4700160";
								break;
							}
							
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "JDQ39":										//3- Nexus 4
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = true;
							metodoRecovery = false;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = false;
							metodoDiscoverPattern = false;
							metodoDeletePass = false;
							metodoAT = true;
							
							idFabricante = "6353";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";

							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/sdcard1";
							rutaParticion = "/dev/block/mmcblk0p24";
							tamanioParticion = 0;
							cantidadBloques = "";
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "JZO54K":										//4- Optimus L7
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = true;
							metodoRecovery = false;
							metodoLgLaf = false;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = false;
							metodoDiscoverPattern = false;
							metodoDeletePass = false;
							metodoAT = true;
							
							idFabricante = "4100";
							rutaDirtyCowDcow = "src/main/resources/devices/dirtycow/lge/lgl7/dirtycow";
							rutaDirtyCowRunas = "src/main/resources/devices/dirtycow/lge/lgl7/run-as";
							
							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/external_SD";
							rutaParticion = "/dev/block/mmcblk0p20";
							tamanioParticion = 1941962752L;//USERDATA
							cantidadBloques = "1896448";
							break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------		
					case "LRX21Y":									
						{
						if(modelo.equals("LG-H440AR"))				//5- Spirit
							{		
							metodoDirtyCowModificado = false;
							metodoDirtyCow = false;
							metodoRecovery = false;
							metodoLgLaf = true;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = false;
							metodoDiscoverPattern = false;
							metodoDeletePass = false;
							metodoAT = true;
							
							idFabricante = "4100";
							rutaDirtyCowDcow = "";
							rutaDirtyCowRunas = "";

							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/external_SD";
							rutaParticion = "/dev/block/mmcblk039";
							tamanioParticion = 3833488384L;
							cantidadBloques = "3743641";
							break;
							}
						else if(modelo.equals("LG-H340AR"))			//6- Leon
						{
							metodoDirtyCowModificado = false;
							metodoDirtyCow = true;
							metodoRecovery = false;
							metodoLgLaf = true;
							metodoMtp = true;
							metodoAdb = true;
							metodoUnlockScreenSamsung = false;
							metodoDiscoverPattern = false;
							metodoDeletePass = false;
							metodoAT = true;
							
							idFabricante = "4100";
							rutaDirtyCowDcow = "src/main/resources/devices/dirtycow/lge/lgleon/dirtycow";
							rutaDirtyCowRunas = "src/main/resources/devices/dirtycow/lge/lgleon/run-as";

							rutaSdcard0 = "/storage/sdcard0";
							rutaSdcard1 = "/storage/external_SD";
							rutaParticion = "/dev/block/mmcblk0p39";
							tamanioParticion = 3833488384L;
							cantidadBloques = "3743641";
						}
						break;
					}
//--------------------------------------------------------------------------------------------------------------------------------------------------					
					case "LMY47V":									//7- K4
					{
						metodoDirtyCowModificado = true;
						metodoDirtyCow = false;
						metodoRecovery = false;
						metodoLgLaf = false;
						metodoMtp = true;
						metodoAdb = true;
						metodoUnlockScreenSamsung = false;
						metodoDiscoverPattern = false;
						metodoDeletePass = false;
						metodoAT = true;
						
						idFabricante = "4100";
						rutaDirtyCowDcow = "";
						rutaDirtyCowRunas = "";
						rutaDirtyCowModificado="src/main/resources/devices/dirtycowmod/adb_push.sh";
						rutaSdcard0 = "/storage/sdcard0";
						rutaSdcard1 = "/storage/external_SD";
						rutaParticion = "/dev/block/mmcblk0p36";
						tamanioParticion = 4596432896L;
						cantidadBloques = "4488704";
						break;
					}
//--------------------------------------------------------------------------------------------------------------------------------------------------					
					case "NRD90U":									//8- K10
					{
						metodoDirtyCowModificado = false;
						metodoDirtyCow = false;
						metodoRecovery = false;
						metodoLgLaf = true;
						metodoMtp = true;
						metodoAdb = true;
						metodoUnlockScreenSamsung = false;
						metodoDiscoverPattern = false;
						metodoDeletePass = false;
						metodoAT = true;
						
						idFabricante = "4100";
						rutaDirtyCowDcow = "";
						rutaDirtyCowRunas = "";

						rutaSdcard0 = "/storage/sdcard0";
						rutaSdcard1 = "/storage/sdcard1";
						rutaParticion = "/dev/block/mmcblk0";
						tamanioParticion = 15762194432L;//TODAS PARTICIONES
						cantidadBloques = "15392768";
						break;
					}
//--------------------------------------------------------------------------------------------------------------------------------------------------					
					case "MXB48T":									//9- X power
					{
						metodoDirtyCowModificado = false;
						metodoDirtyCow = false;
						metodoRecovery = false;
						metodoLgLaf = true;
						metodoMtp = true;
						metodoAdb = true;
						metodoUnlockScreenSamsung = false;
						metodoDiscoverPattern = false;
						metodoDeletePass = false;
						metodoAT = true;
						
						idFabricante = "4100";
						rutaDirtyCowDcow = "";
						rutaDirtyCowRunas = "";

						rutaSdcard0 = "/storage/sdcard0";
						rutaSdcard1 = "/storage/external_SD";
						rutaParticion = "/dev/block/mmcblk0p40";
						tamanioParticion = 10877403136L;//USERDATA
						cantidadBloques = "10622464";
						break;
					}
					
//--------------------------------------------------------------------------------------------------------------------------------------------------					
				}
				break;
			}
//--------------------------------------------------------------------------------------------------------------------------------------------------					
//##################################################################################################################################################		
			case "motorola":
			{
				idFabricante = "8888";
				rutaMtp = "";
				rutaLglaf = "";
				rutaLglafExtractPartitions = "";
				rutaLglafPartitions = "";
				cantidadBloques = "";
				tamanioLogico = 0;
				switch(numeroCompilacion)
				{
//--------------------------------------------------------------------------------------------------------------------------------------------------					
					case "9.8.2I-50_SML-23":								//1- Razr I
						{
						metodoDirtyCowModificado = false;
						metodoDirtyCow = true;
						metodoRecovery = false;
						metodoLgLaf = false;
						metodoMtp = true;
						metodoAdb = true;
						metodoUnlockScreenSamsung = false;
						metodoDiscoverPattern = false;
						metodoDeletePass = false;
						metodoAT = false;
						
						rutaDirtyCowDcow = "src/main/resources/devices/dirtycow/motorola/XT890/dirtycow";
						rutaDirtyCowRunas = "src/main/resources/devices/dirtycow/motorola/XT890/run-as";
					
						rutaSdcard0 = "/storage/sdcard0";
						rutaSdcard1 = "/storage/sdcard1";
						rutaParticion = "/dev/block/mmcblk0p17";
						tamanioParticion = 5599640576L;
						cantidadBloques = "";
						break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------								
					case "2_32A_2017":										//2- Razr D1
					{
						metodoDirtyCowModificado = false;
						metodoDirtyCow = true;
						metodoRecovery = false;
						metodoLgLaf = false;
						metodoMtp = true;
						metodoAdb = true;
						metodoUnlockScreenSamsung = false;
						metodoDiscoverPattern = false;
						metodoDeletePass = false;
						metodoAT = false;
						
						rutaDirtyCowDcow = "src/main/resources/devices/dirtycow/motorola/XT915/dirtycow";
						rutaDirtyCowRunas = "src/main/resources/devices/dirtycow/motorola/XT915/run-as";
					
						rutaSdcard0 = "/storage/sdcard0";
						rutaSdcard1 = "/storage/sdcard1";
						rutaParticion = "/dev/block/mmcblk0p3";
						tamanioParticion = 2620391424L;
						cantidadBloques = "2558976";
						break;
					}
//--------------------------------------------------------------------------------------------------------------------------------------------------					
					case "LPBS23.13-56-2":									//3- G
					{	
						metodoDirtyCowModificado = true;
						metodoDirtyCow = false;
						metodoRecovery = false;
						metodoLgLaf = false;
						metodoMtp = true;
						metodoAdb = true;
						metodoUnlockScreenSamsung = false;
						metodoDiscoverPattern = false;
						metodoDeletePass = false;
						metodoAT = false;
						
						rutaDirtyCowDcow = "";
						rutaDirtyCowRunas = "";
						
						rutaDirtyCowModificado="src/main/resources/devices/dirtycowmod/adb_push.sh";

						rutaSdcard0 = "/storage/sdcard0";
						rutaSdcard1 = "";
						rutaParticion = "/dev/block/mmcblk0p36";
						tamanioParticion = 5930614784L;
						cantidadBloques = "5791616";
						break;
					}
//--------------------------------------------------------------------------------------------------------------------------------------------------					
					case "KXC21.5-40":										//4- X
					{
						metodoDirtyCowModificado = false;
						metodoDirtyCow = true;
						metodoRecovery = false;
						metodoLgLaf = false;
						metodoMtp = true;
						metodoAdb = true;
						metodoUnlockScreenSamsung = false;
						metodoDiscoverPattern = false;
						metodoDeletePass = false;
						metodoAT = false;
						
						rutaDirtyCowDcow = "src/main/resources/devices/dirtycow/motorola/XT1021/dirtycow";
						rutaDirtyCowRunas = "src/main/resources/devices/dirtycow/motorola/XT1021/run-as";

						rutaSdcard0 = "/storage/sdcard0";
						rutaSdcard1 = "/storage/sdcard1";
						rutaParticion = "/dev/block/mmcblk0p36";
						tamanioParticion = 2364932096L;
						cantidadBloques = "2309504";
						break;
					}
//--------------------------------------------------------------------------------------------------------------------------------------------------					
				}
				break;
			}
//--------------------------------------------------------------------------------------------------------------------------------------------------					
//##################################################################################################################################################		
			case "Sony":
			{
				idFabricante = "4046";
				rutaMtp = "";
				rutaLglaf = "";
				rutaLglafExtractPartitions = "";
				rutaLglafPartitions = "";
				cantidadBloques = "";
				tamanioLogico = 0;
				switch(numeroCompilacion)
				{
//--------------------------------------------------------------------------------------------------------------------------------------------------					
				case "15.3.A.1.17":										//1- Xperia L
						{
						metodoDirtyCowModificado = false;
						metodoDirtyCow = true;
						metodoRecovery = false;
						metodoLgLaf = false;
						metodoMtp = true;
						metodoAdb = true;
						metodoUnlockScreenSamsung = false;
						metodoDiscoverPattern = false;
						metodoDeletePass = false;
						metodoAT = false;
						
						rutaDirtyCowDcow = "src/main/resources/devices/dirtycow/sony/C2104/dirtycow";
						rutaDirtyCowRunas = "src/main/resources/devices/dirtycow/sony/C2104/run-as";

						rutaSdcard0 = "/storage/sdcard0";
						rutaSdcard1 = "/storage/sdcard1";
						rutaParticion = "/dev/block/mmcblk0p32";
						tamanioParticion = 4311727104L;
						cantidadBloques = "";
						break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------					
				case "18.6.A.0.182":										// Xperia M2
						{
						metodoDirtyCowModificado = false;
						metodoDirtyCow = false;
						metodoRecovery = false;
						metodoLgLaf = false;
						metodoMtp = true;
						metodoAdb = true;
						metodoUnlockScreenSamsung = false;
						metodoDiscoverPattern = false;
						metodoDeletePass = false;
						metodoAT = false;
						
						rutaDirtyCowDcow = "";
						rutaDirtyCowRunas = "";

						rutaSdcard0 = "/storage/sdcard0";
						rutaSdcard1 = "/storage/sdcard1";
						rutaParticion = "/dev/block/mmcblk0p29";
						tamanioParticion = 5461000192L; 	//5333008
						cantidadBloques = "";
						break;
						}
//--------------------------------------------------------------------------------------------------------------------------------------------------					
				}
				break;
			}	
//--------------------------------------------------------------------------------------------------------------------------------------------------					
//##################################################################################################################################################		
			default:
			{
				metodoDirtyCowModificado = false;
				metodoDirtyCow = false;
				metodoRecovery = false;
				metodoLgLaf = false;
				metodoAdb = false;
				metodoMtp = true;
				metodoUnlockScreenSamsung = false;
				metodoDiscoverPattern = false;
				metodoDeletePass = false;
				metodoAT = false;
				idFabricante = "";
				rutaParticion = "";
				rutaDirtyCowDcow = "";
				rutaDirtyCowRunas = "";
				rutaRecoveryFabrica = "";
				rutaRecoveryPersonalizado = "";
				rutaLglaf = "";
				rutaLglafPartitions = "";
				rutaLglafExtractPartitions = "";
				rutaMtp = "";
				rutaSdcard0 = "";
				rutaSdcard1 = "";
				cantidadBloques = "";
				tamanioParticion = 0;
				tamanioLogico = 0;
			}
		}
	}
//--------------------------------------------------------------------------------------------------------------------------------------------------					
	/**
	 * Esta función, retorna fabricante de instancia dispositivo Android.
	 * @return Retorna variable String llamada fabricante que contiene
	 * nombre de fabricante de instancia dispositivo Android.
	 */
	public String getFabricante()
	{
		return fabricante;
	}
	
	/**
	 * Esta función, retorna modelo de instancia dispositivo Android.
	 * @return Retorna una variable String llamada modelo que contiene
	 * nombre de modelo de instancia dispositivo Android.
	 */
	public String getModelo()
	{
		return modelo;
	}
	
	/**
	 * Esta función, retorna versión de sistema operativo de instancia dispositivo Android.
	 * @return Retorna variable String llamada versionSo que contiene
	 * versión de sistema operativo de instancia dispositivo Android.
	 */
	public String getVersionSo()
	{
		return versionSo;
	}
	
	/**
	 * Esta funcion, retorna numero de compilacion de instancia dispositivo Android.
	 * @return Retorna variable String llamada numeroCompilacion que contiene
	 * numero de compilacion de instancia dispositivo Android.
	 */
	public String getNumeroCompilacion()
	{
		return numeroCompilacion;
	}
	
	/**
	 * Esta función, retorna id vendor de instancia dispositivo Android.
	 * @return Retorna variable String llamada idFabricante que contiene
	 * identificador de fabricante de instancia dispositivo Android.
	 */
	public String getIdFabricante()
	{
		return idFabricante;
	}
	
	/**
	 * Esta función, retorna booleano de instancia dispositivo Android.
	 * Si es true indica que instancia Dispositivo se seleccionó.
	 * Si es false indica que instancia Dispositivo no se seleccionó.
	 * @return Retorna variable boolean llamada seleccionado
	 * que indica si se seleccionó o no dispositivo Android.
	 */
	public boolean getSeleccionado()
	{
		return seleccionado;
	}
	
	/**
	 * Esta función, proporciona tamaño de partición de instancia dispositivo Android.
	 * @return Retorna variable long llamada tamanioParticion que contiene
	 * tamaño de partición de instancia dispositivo Android.
	 */
	public long getTamanioParticion()
	{
		return tamanioParticion;
	}
	
	/**
	 * Esta función, retorna booleano de instancia dispositivo Android.
	 * Si es true indica que soporta método Dirty Cow, instancia creada.
	 * Si es false indica que no soporta método Dirty Cow, instancia creada.
	 * @return Retorna variable boolean llamada metodoDirtyCow
	 * que indica si soporta o no soporta método Dirty Cow.
	 */
	public boolean getMetodoDirtyCow()
	{
		return metodoDirtyCow;
	}
	
	public boolean getMetodoDirtyCowModificado()
	{
		return metodoDirtyCowModificado;
	}
	/**
	 * Esta función, retorna booleano de instancia dispositivo Android.
	 * Si es true indica que soporta método Recovery, instancia creada.
	 * Si es false indica que no soporta método Recovery, instancia creada.
	 * @return Retorna variable boolean llamada metodoRecovery
	 * que indica si soporta o no soporta método Recovery.
	 */
	public boolean getMetodoRecovery()
	{
		return metodoRecovery;
	}
	
	/**
	 * Esta función, retorna booleano de instancia dispositivo Android.
	 * Si es true indica que soporta método Lg laf, instancia creada.
	 * Si es false indica que no soporta método Lg laf, instancia creada.
	 * @return Retorna variable boolean llamada metodoLgLaf
	 * que indica si soporta o no soporta método Lg laf.
	 */
	public boolean getMetodoLgLaf()
	{
		return metodoLgLaf;
	}
	
	/**
	 * Esta función, retorna booleano de instancia dispositivo Android.
	 * Si es true indica que soporta método Adb, instancia creada.
	 * Si es false indica que no soporta método Adb, instancia creada.
	 * @return Retorna variable boolean llamada metodoAdb
	 * que indica si soporta o no soporta método Adb.
	 */
	public boolean getMetodoAdb()
	{
		return metodoAdb;
	}
	
	/**
	 * Esta función, retorna un booleano de instancia dispositivo Android.
	 * Si es true indica que soporta método Mtp, instancia creada.
	 * Si es false indica que no soporta método Mtp, instancia creada.
	 * @return Retorna una variable boolean llamada metodoMtp
	 * que indica si soporta o no soporta método Mtp.
	 */
	public boolean getMetodoMtp()
	{
		return metodoMtp;
	}
	
	public boolean getMetodoUnlockScreenSamsung ()
	{
		return metodoUnlockScreenSamsung;
	}
	
	public boolean getmetodoDiscoverPattern ()
	{
		return metodoDiscoverPattern;
	}	
	
	public boolean getmetodoDeletePass ()
	{
		return metodoDeletePass;
	}	
	
	public boolean getmetodoAT ()
	{
		return metodoAT;
	}	
	
	/**
	 * Esta función, retorna ruta de almacenamiento interno asociada a dispositivo Android conectado.
	 * @return Retorna variable String llamada rutaSdcard0
	 * que contiene ruta de almacenamiento interno asociada a dispositivo Android conectado.
	 */
	public String getRutaSdcard0()
	{
		return rutaSdcard0;
	}
	
	/**
	 * Esta función, retorna ruta de almacenamiento externo asociada a dispositivo Android conectado.
	 * @return Retorna variable String llamada rutaSdcard1
	 * que contiene ruta de almacenamiento externo asociada a dispositivo Android conectado.
	 */
	public String getRutaSdcard1()
	{
		return rutaSdcard1;
	}
	
	/**
	 * Esta función, retorna ruta donde se aloja partición de dispositivo Android.
	 * @return Retorna variable String llamada rutaParticion
	 * que contiene rutarun-as donde se aloja partición de dispositivo Android.
	 */
	public String getRutaParticion()
	{
		return rutaParticion;
	}
	
	/**
	 * Esta función, retorna ruta donde se aloja recovery de fábrica de dispositivo Android.
	 * @return Retorna variable String llamada rutaRecoveryFabrica
	 * que contiene ruta donde se aloja el recovery de fábrica de dispositivo Android.
	 */
	public String getRutaRecoveryFabrica()
	{
		return rutaRecoveryFabrica;
	}
	
	/**
	 * Esta función, retorna ruta donde se aloja recovery personalizado de dispositivo Android.
	 * @return Retorna variable String llamada rutaRecoveryPersonalizado
	 * que contiene ruta donde se aloja recovery de personalizado de dispositivo Android.
	 */
	public String getRutaRecoveryPersonalizado()
	{
		return rutaRecoveryPersonalizado;
	}
	
	/**
	 * Esta función, retorna ruta donde se aloja archivo dirtycow asociado a dispositivo Android conectado.
	 * @return Retorna variable String llamada rutaDirtyCowDcow
	 * que contiene ruta donde se aloja archivo dirtycow asociado a dispositivo Android conectado.
	 */
	public String getRutaDirtyCowDcow()
	{
		return rutaDirtyCowDcow;
	}

	/**
	 * Esta función, retorna ruta donde se aloja archivo run-as asociado a dispositivo Android conectado.
	 * @return Retorna variable String llamada rutaDirtyCowRunas
	 * que contiene ruta donde se aloja archivo run-as asociado a dispositivo Android conectado.
	 */
	public String getRutaDirtyCowRunas()
	{
		return rutaDirtyCowRunas;
	}
	/**
	 * Esta función, retorna ruta donde se aloja archivo adb_push asociado a dispositivo Android conectado.
	 * @return Retorna variable String llamada rutaDirtyCowModificado
	 * que contiene ruta donde se aloja archivo adb_push asociado a dispositivo Android conectado.
	 */
	public String getRutaDirtyCowModificado()
	{
		return rutaDirtyCowModificado;
	}
	
	/**
	 * Esta función, retorna ruta mtp asociada a dispositivo Android conectado.
	 * @return Retorna variable String llamada rutaMtp
	 * que contiene ruta mtp asociada a dispositivo Android conectado.
	 */
	public String getRutaMtp()
	{
		return rutaMtp;
	}
	
	/**
	 * Esta función, proporciona tamaño de almacenamiento tanto interno como externo de instancia dispositivo Android.
	 * @return Retorna variable long llamada tamanioLogico que contiene
	 * tamaño de directorio de almacenamiento interno y externo de instancia dispositivo Android.
	 */
	public long getTamanioLogico()
	{
		return tamanioLogico;
	}
	
	/**
	 * Esta función, retorna ruta de archivo lglaf.py asociada a dispositivo Android conectado.
	 * @return Retorna variable String llamada rutaLglaf
	 * que contiene ruta de archivo lglaf.py asociada a dispositivo Android conectado.
	 */
	public String getRutaLglaf()
	{
		return rutaLglaf;
	}
	
	
	/**
	 * Esta función, retorna ruta de archivo partitions.py asociada a dispositivo Android conectado.
	 * @return Retorna variable String llamada rutaLglafPartitions
	 * que contiene ruta de archivo partitions.py asociada a dispositivo Android conectado.
	 */
	public String getRutaLglafPartitions()
	{
		return rutaLglafPartitions;
	}
	
	/**
	 * Esta función, retorna ruta de archivo extract-partitions.py asociada a dispositivo Android conectado.
	 * @return Retorna variable String llamada rutaLglafExtractPartitions
	 * que contiene ruta de archivo extract-partitions.py asociada a dispositivo Android conectado.
	 */
	public String getRutaLglafExtractPartitions()
	{
		return rutaLglafExtractPartitions;
	}
	
	/**
	 * Esta función, retorna cantidad de bloques asociada a particion de dispositivo Android.
	 * @return Retorna variable String llamada cantidadBloques
	 * que contiene cantidad de bloques asociada a partición de dispositivo Android.
	 */
	public String getCantidadBloques()
	{
		return cantidadBloques;
	}
	
	/**
	 *Este método, permite setear en true o false variable booleana seleccionado.
	 * @param seleccionado Variable booleana que tiene cada instancia Dispositivo.
	 * Indica si es seleccionado (true) o no seleccionado (false).
	 */
	public void setSeleccionar(boolean seleccionado)
	{
		this.seleccionado = seleccionado; 
	}
	
	/**
	 *Este metodo, permite setear variable String rutaMtp.
	 * @param rutaMtp Variable String que tiene cada instancia Dispositivo.
	 * Indica ruta mtp asociada a dispositivo Android conectado.
	 */
	public void setRutaMtp(String numeroBus, String numeroDispositivo)
	{
		this.rutaMtp = "/run/user/1000/gvfs/mtp:host=%5Busb%3A" + numeroBus + "%2C" + numeroDispositivo + "%5D/";
	}
	
	/**
	 *Este método, permite setear variable long tamanioLogico.
	 * @param tamanioLogico Variable long que tiene cada instancia Dispositivo.
	 * Indica tamaño de almacenamiento de dispositivo Android conectado.
	 */
	public void setTamanioLogico(long tamanioLogico)
	{
		this.tamanioLogico = tamanioLogico;
	}
}
