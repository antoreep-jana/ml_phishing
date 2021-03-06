        /*
         * To change this license header, choose License Headers in Project Properties.
         * To change this template file, choose Tools | Templates
         * and open the template in the editor.
         */

        /**
         *
         * @author antor
         */
        import java.util.*;

        import weka.core.converters.ConverterUtils.DataSource;    
        import java.util.Random;
        import weka.filters.Filter;
        import weka.filters.unsupervised.attribute.Remove;

        import java.io.File;
        import java.io.FileReader;
        import java.io.BufferedReader;
        import java.io.FileNotFoundException;
        import java.io.IOException;


        import weka.classifiers.meta.AdaBoostM1;
        import weka.classifiers.meta.Bagging;
        import weka.classifiers.meta.Vote;
        import weka.classifiers.Classifier;
        import weka.classifiers.Evaluation;
        import weka.classifiers.bayes.NaiveBayes;
        import weka.classifiers.evaluation.NominalPrediction;
    import weka.classifiers.functions.Logistic;
        import weka.classifiers.functions.SMO;
    import weka.classifiers.lazy.IBk;
    import weka.classifiers.lazy.KStar;
        import weka.classifiers.rules.DecisionTable;
        import weka.classifiers.rules.OneR;
        import weka.classifiers.rules.PART;
        import weka.classifiers.trees.DecisionStump;
        import weka.classifiers.trees.J48;
        import weka.classifiers.meta.Stacking;
    import weka.classifiers.rules.JRip;
    import weka.classifiers.rules.ZeroR;
    import weka.classifiers.trees.LMT;
    import weka.classifiers.trees.REPTree;
    import weka.classifiers.trees.RandomForest;
    import weka.classifiers.trees.RandomTree;



        import weka.core.FastVector;
        import weka.core.Instances;

        import weka.core.converters.ArffSaver;
        import weka.core.converters.CSVLoader;
        import weka.core.converters.ArffLoader;
        import weka.filters.supervised.instance.StratifiedRemoveFolds;


        public class ClassifiersMenu {




            public static Evaluation classify(Classifier model, Instances trainingSet, Instances testingSet) throws Exception {

                Evaluation validation = new Evaluation(trainingSet);

                model.buildClassifier(trainingSet);
                validation.evaluateModel(model, trainingSet);

                return validation;

            }




            public static void main(String[] args) throws IOException, Exception{
               char ch;

                // 1. Taking the input file from the user
                Scanner s = new Scanner(System.in);
                System.out.println("Open the file for performing classfication");
                System.out.println("Type file location : ");
                String a = new String();
                a = s.nextLine();

                ArffLoader arffLoader = new ArffLoader();

                try{
                    arffLoader.setSource(new File(a));
                }catch(FileNotFoundException ex){
                    System.err.println("File not found" + a);
                }
                Instances data = arffLoader.getDataSet();
                data.setClassIndex(data.numAttributes()-1);
                int classIndex = data.numAttributes()-1;        
                // Dividing the dataset into training and testing dataset

                // stratified remove folds to randomly split data
                StratifiedRemoveFolds filter = new StratifiedRemoveFolds();

                // set options for creating the subset of data

                String[] options = new String[6];
                int N,S;
                System.out.println("Select the number of folds");
                N = s.nextInt();
                System.out.println("What random seed do you want to set?");
                S = s.nextInt();

                    options[0] = "-N";                 // indicate we want to set the number of folds                        
                    options[1] = Integer.toString(N);  // split the data into five random folds
                    options[2] = "-F";                 // indicate we want to select a specific fold
                    options[3] = Integer.toString(1);  // select the first fold
                    options[4] = "-S";                 // indicate we want to set the random seed
                    options[5] = Integer.toString(S); 

                filter.setOptions(options);
                filter.setInputFormat(data);


                // test data
                filter.setInvertSelection(false);
                //applying filter for test data here
                Instances test = Filter.useFilter(data,filter);


                //train data
                filter.setInvertSelection(true);
                Instances train = Filter.useFilter(data, filter);

                           int choice = -1;

                do{
                //2. Preprocessing

                  System.out.println("Do you want to preprocess the data (using remove FIlter)? y/n");
                  ch = s.next().charAt(0);
                  if(ch == 'y' || ch == 'Y'){
                   System.out.println("Enter the indices of columns to be used");
                   ArrayList<Integer> list = new ArrayList<Integer>();
                   int ind = 0;
                   while(ind!=-1){
                       ind = s.nextInt();
                       if(ind==-1)
                           break;
                       list.add(ind);
                   }

                   int[] indicesOfColumnsToUse = new int[list.size()]; 
                   for(int i = 0 ; i < list.size() ; i++){
                       indicesOfColumnsToUse[i] = list.get(i);
                   }

                    // using remove filter
                    Remove remove = new Remove();
                    remove.setAttributeIndicesArray(indicesOfColumnsToUse);
                    System.out.println("Set Invert Selection ? y/n");

                    ch = s.next().charAt(0);
                    if(ch == 'y' || ch == 'Y')
                        remove.setInvertSelection(true);
                    else 
                        remove.setInvertSelection(false);

                    remove.setInputFormat(train);

                   Instances trainingSubset = Filter.useFilter(train,remove);

                  // Classifier cls = new J48();
                //    cls.buildClassifier(trainingSubset);
                 //  Evaluation eval = new Evaluation(train);
                  //   eval.evaluateModel(cls, test);


                     }

                  // Applying classifiers
                  System.out.println("***********************");
                  System.out.println("Now select the classifier you want to apply :-");
                  System.out.println();
                  System.out.println("1.Bayes ( Naive Bayes)");
                  System.out.println("2.Trees (J48, Decision Stump, Random Forest, LMT, REPTree)");
                  System.out.println("3. Functions (SMO, Logistic, LibSVM)");
                  System.out.println("4. Rules (ZeroR, OneR, PART, JRip, Decision Table ");
                  System.out.println("5. Lazy (IBK, KStar");
                  System.out.println("6. Meta Classifiers (AdaBoost, Bagging, Stacking)");
                  System.out.println("*************************");
                  System.out.println();
                  FastVector predictions = new FastVector();
                  choice = s.nextInt();
                  switch(choice){

                      case 1:
                           Evaluation validation = classify(new NaiveBayes(), trainingSplits[j],testingSplits[j]);
                           predictions.appendElements(validation.predictions());
                            break;
                      case 2:{
                            System.out.println();
                            System.out.println("Choose one among the following :");
                            System.out.println("a. J48");
                            System.out.println("b. Decision Stump");
                            System.out.println("c. Random Forest");
                            System.out.println("d. LMT");
                            System.out.println("e. REPTree");
                            System.out.println("Type your choice from one among the above options :");
                            char choice1 = s.next().charAt(0);
                            switch(choice1){
                                case 'a':
                                    // J48 
                                {
                                    Evaluation eval = classify(new J48(), train, test);
                                    predictions.appendElements(eval.predictions());
                                    System.out.println();
                                    System.out.println(eval.fMeasure(classIndex));} // fmeasure
                                    break;
                                case 'b':
                                    //Decision Stump
                                    {
                                    Evaluation eval = classify(new DecisionStump(), train, test);
                                    predictions.appendElements(eval.predictions());
                                    System.out.println();
                                    System.out.println(eval.fMeasure(classIndex));
                                     }
                                    break;
                                case 'c':
                                    // Random Forest
                                { Evaluation eval = classify(new RandomForest(), train, test);
                                    predictions.appendElements(eval.predictions());
                                    System.out.println();
                                    System.out.println(eval.fMeasure(classIndex));
                                        }
                                    break;
                                case 'd':
                                    // LMT
                                {
                                 Evaluation eval = classify(new LMT(), train, test);
                                 predictions.appendElements(eval.predictions());
                                 System.out.println();
                                 System.out.println(eval.fMeasure(classIndex));
                                }


                                    break;
                                case 'e':
                                    //REPTree
                                    {
                                    Evaluation eval = classify(new REPTree() , train, test);
                                    predictions.appendElements(eval.predictions());
                                    System.out.println();
                                    System.out.println(eval.fMeasure(classIndex));
                                    }

                                    break;
                            }

                            break;}
                      case 3:
                            // Functions
                            {
                          System.out.println();
                          System.out.println("Choose one among the following: ");
                          System.out.println("a. SMO");
                          System.out.println("b. Logistic");
                          //System.out.println("c. LibSVM");
                          System.out.println("Type your choice from one among the above:");
                          char choice1 = s.next().charAt(0);
                          switch(choice1){
                              case 'a':
                                  // SMO
                              {
                                  Evaluation eval = classify(new SMO(), train, test);
                                  predictions.appendElements(eval.predictions());
                                  System.out.println();
                                  System.out.println(eval.fMeasure(classIndex));
                              }
                                  break;
                              case 'b':
                                  //Logistic
                              {   
                                     Evaluation eval = classify(new Logistic(), train, test);
                                  predictions.appendElements(eval.predictions());
                                  System.out.println();
                                  System.out.println(eval.fMeasure(classIndex));
                              }
                                  break;


                          }


                            break;}
                       case 4:
                        // Rules
                           System.out.println();
                           System.out.println("Choose one among the following classifiers");
                           System.out.println("a. ZeroR");
                           System.out.println("b. OneR");
                           System.out.println("c. PART");
                           System.out.println("d. JRip");
                           System.out.println("e. Decision Table");
                           System.out.println("Type your choice from the options above");
                           {
                               char choice1 = s.next().charAt(0);
                               switch(choice1){
                                   case 'a':
                                       // ZeroR
                                   {
                                       Evaluation eval = classify(new ZeroR(), train, test);
                                  predictions.appendElements(eval.predictions());
                                  System.out.println();
                                  System.out.println(eval.fMeasure(classIndex));

                                   }
                                       break;
                                   case 'b':
                                       // OneR
                                   {
                                       Evaluation eval = classify(new OneR(), train, test);
                                  predictions.appendElements(eval.predictions());
                                  System.out.println();
                                  System.out.println(eval.fMeasure(classIndex));
                                   }
                                       break;

                                   case 'c':
                                       //PART
                                   {
                                       Evaluation eval = classify(new PART(), train, test);
                                  predictions.appendElements(eval.predictions());
                                  System.out.println();
                                  System.out.println(eval.fMeasure(classIndex));
                                   }

                                       break;
                                   case 'd':
                                       //JRip
                                   {
                                  Evaluation eval = classify(new JRip(), train, test);
                                  predictions.appendElements(eval.predictions());
                                  System.out.println();
                                  System.out.println(eval.fMeasure(classIndex));

                                   }


                                       break;

                                   case 'e':
                                       //Decision Table
                                   {
                                  Evaluation eval = classify(new DecisionTable(), train, test);
                                  predictions.appendElements(eval.predictions());
                                  System.out.println();
                                  System.out.println(eval.fMeasure(classIndex));
                                   }


                                       break;




                               }



                           }     



                          break;
                      case 5:
                                    //Lazy Classifiers
                            System.out.println();
                            System.out.println("Choose one among the following classifiers");
                            System.out.println("a. IBK");
                            System.out.println("b. KStar");
                            System.out.println("Type one among the above classifiers you want to apply");
                            //System.out.println("a. J48");
                            {

                                char choice1 = s.next().charAt(0);
                                switch(choice1){
                                    case 'a':
                                        // IBK
                                    {
                                        Evaluation eval = classify(new IBk(), train, test);
                                  predictions.appendElements(eval.predictions());
                                  System.out.println();
                                  System.out.println(eval.fMeasure(classIndex));
                                    }
                                        break;
                                    case 'b':
                                        // KStar
                                    {
                                        Evaluation eval = classify(new KStar(), train, test);
                                  predictions.appendElements(eval.predictions());
                                  System.out.println();
                                  System.out.println(eval.fMeasure(classIndex));
                                    }
                                        break;

                                }


                              }  
                          break;
                      case 6:{
                          // Only for Meta Classifiers
                          System.out.println("Choose one among the following : ");
                          System.out.println("s. Stacking");
                          System.out.println("b. Bagging");
                          System.out.println("a. ADABoost");
                          System.out.println(" Type one among (a, b, s) :");
                          char choice1;
                          choice1 = s.next().charAt(0);
                          switch(choice1){    
                            case 'a':
                                AdaBoostM1 m1 = new AdaBoostM1();
                                m1.setClassifier(new DecisionStump()); // base Classifier
                                m1.setNumIterations(20);
                            m1.buildClassifier(train);


                            break;
                        case 'b':
                            // Bagging
                            Bagging bagger = new Bagging();
                            bagger.setClassifier(new RandomTree()); // .. Base Model
                            bagger.setNumIterations(25);
                            bagger.buildClassifier(train);


                            break;
                        case 's': 
                          String[] stackoptions = new String[1];
                          {
                              stackoptions[0] = "-w weka.classifiers.functions.SMO";
                          }
                          Stacking nb = new Stacking();
                          J48 j48 = new J48();
                          SMO smo = new SMO();
                          Classifier[] stackoptionsbase = new Classifier[1];
                          stackoptionsbase[0] = smo;
                          nb.setClassifiers(stackoptionsbase);
                          nb.setMetaClassifier(j48);
                          nb.buildClassifier(train);
                          Evaluation eval = new Evaluation(train);
                          eval.crossValidateModel(nb, train, 10, new Random(1));
                          System.out.println(eval.toSummaryString("results",true));
                      }  
                          break;}
                      default: System.out.println("Not among [1-5]");
                          return;
                  }  


                }while(choice!=9);

            }



        }
