### 代码原理理解

1. 首先在`encoder`文件夹下，查看生成隐写术图的主程序，在`main`函数中调用了`getSteganography()`函数

   ```java
   /*该函数封装了许多功能，对代码进行逐行解释*/
   public static void getSteganography(String classSource, String originImage) throws IOException {
           //获取排序类的类名
       	String className = classSource.substring(0,classSource.indexOf(".")).replace("/", ".");
       	//使用JavaCompiler类对排序类的文件进行动态编译，得到.class文件
           SteganographyFactory.compile(classSource);
           //使用ImageIO的read函数读取原图
       	BufferedImage image = ImageIO.read(new File(originImage));
       	//初始化Encoder类，传入图片参数
           SteganographyEncoder steganographyEncoder = new SteganographyEncoder(image);
   		//使用Encoder类的encodeFile函数，将编译好的排序文件的字节码编码进图片中
           BufferedImage encodedImage = steganographyEncoder.encodeFile(new File(classSource.replace("java", "class")));
           //使用ImageIO的write函数将已编码的image写出，得到隐写术图
       	ImageIO.write(encodedImage, "png", new File(className+".png"));
   
       }
   ```

2. 得到一张隐写术图后，在`Scene`的`main`函数中执行主程序

   `SteganographyClassLoader loader `：加载隐写术图，并调用`loadClass()`函数尝试获取Sorter相关的class，根据类加载机制，`ClassLoader`将向上委派，由父类尝试加载，父类加载失败后，再由子类尝试加载，我们期望`Bootstrap ClassLoader、Extention ClassLoader`和`Applicaion ClassLoader`均找不到`BubbleSorter`类，最终由自定义的类加载器根据重写的`findClass`方法，加载隐写术图中的字节码得到`BubbleSorter`类。

   根据此类加载机制，则应注意在本实验中，为了达到我们的期望，需要在运行`Scene`的`main`函数前先将`BubbleSorter.java`和`BubbleSorter.class`文件删除，否则`ApplicationClassLoader`将根据`classpath`找到本地文件中的`BubbleSorter`类，那么自定义的类加载器将不会执行。

3. 自定义类加载器中的`findClass()`

   该函数将根据图片的url读取图片信息，并对图片进行解码，将得到的图片中隐含的字节码用`defineClass`函数加载到类中。

   ```java
   @Override
       protected Class<?> findClass(String name) throws ClassNotFoundException {
           try {
               BufferedImage img = ImageIO.read(url);
               SteganographyEncoder encoder = new SteganographyEncoder(img);
               byte[] bytes = encoder.decodeByteArray();
               return this.defineClass(name, bytes, 0, bytes.length);
           } catch (Exception e) {
               throw new ClassNotFoundException();
           }
       }
   ```

4. `newInstance()`：创建load的类的实体，实现`sorter`接口。

### **图片的url**:

**SelectSorter**:https://github.com/jwork-2021/jw03-kingyaaa/blob/main/example/resources/select.jpeg

**QuickSorter**:https://github.com/jwork-2021/jw03-kingyaaa/blob/main/example/resources/quick.jpeg

[实验时使用了本地文件的绝对路径]

**SelectSorter**: file:///home/njucs/Java/jw03-kingyaaa/example.BubbleSorter.png

**QuickSorter**: file:///home/njucs/Java/jw03-kingyaaa/example.QuickSorter.png 

### 排序结果的动画链接

**SelectSorter**:https://asciinema.org/a/2a9Y3Ja5Juib8820o1jFgiBMm

**QuickSorter**:https://asciinema.org/a/Y9xM1zmyWae78hWGKqv2vtHn9

### 使用同学的图片进行排序

在仓库中随机选取了账号为**BanpinLi**的同学的图片

1. 图片url为https://github.com/jwork-2021/jw03-BanpinLi/blob/main/example.QuickSort.png

   排序结果正确

   ![image-20211005105400778](C:\Users\10513\AppData\Roaming\Typora\typora-user-images\image-20211005105400778.png)

2. 图片url为https://github.com/jwork-2021/jw03-BanpinLi/blob/main/example.SelectSort.png

   排序结果正确

   ![image-20211005105913974](C:\Users\10513\AppData\Roaming\Typora\typora-user-images\image-20211005105913974.png)
