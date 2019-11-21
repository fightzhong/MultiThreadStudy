import java.io.File;

/**
 * 通过提供一个根目录, 遍历目录下所有的子文件及子文件夹, 获取所有文件名,
 * 并生成gitbook的summary.md目录格式
 */
public class GenerateDirectoryStructure {
	public static void main (String[] args) {
		File file = new File( "C:\\Users\\zhongshenglong\\Desktop\\Notes\\MultiThreadStudy\\src\\main\\resources" );
		printStructure( file, 0 );
	}

	public static void printStructure (File file, int deep) {
		if ( !file.exists() ) throw new RuntimeException();
		if ( file.getName().equals( "photos" ) ) return;

		// 获取深度字符串
		String deepStr = generateDeepDescription(deep);
		String description;
		// 如果是一个文件夹
		if ( file.isDirectory() ) {
			// 文件夹直接输出文件名
			description = deepStr + "* ["+ file.getName() +"]()";
			System.out.println( description );
			// 遍历该文件夹下的所有文件并递归调用printStructure方法
			File[] allFile = file.listFiles();
			if ( allFile.length == 0 ) {
				return;
			}
			for ( int i = 0; i < allFile.length; i ++ ) {
				printStructure( allFile[i], deep + 1 );
			}
		} else { // 如果是一个文件
			description = deepStr + "* ["+ file.getName() +"]("+ removeThePrefix( file.getAbsolutePath() ) +")";
			System.out.println( description );
		}

	}

	public static String generateDeepDescription (int deep) {
		StringBuilder builder = new StringBuilder();
		for ( int i = 0; i < deep; i ++ ) {
			builder.append( "  " );
		}
		return builder.toString();
	}

	// 将字符串的.md移除
	// public static String removeThePostfix (String str) {
	// 	if ( str.endsWith( ".md" ) ) {
	// 		int lastIndex = str.lastIndexOf(".md");
	// 		return str.substring( 0, lastIndex );
	// 	}
	// 	return str;
	// }

	// 将字符串的前缀C:\Users\zhongshenglong\Desktop\Notes\MultiThreadStudy\移除
	public static String removeThePrefix (String str) {
		if ( str.startsWith( "C:\\Users\\zhongshenglong\\Desktop\\Notes\\MultiThreadStudy\\" ) ) {
			return str.replace( "C:\\Users\\zhongshenglong\\Desktop\\Notes\\MultiThreadStudy\\", "" );
		}
		return str;
	}
}
