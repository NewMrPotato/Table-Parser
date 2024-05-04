package com.rather.parsoftables.Controllers;

import com.rather.parsoftables.Application;
import com.rather.parsoftables.FileManagement.ExcelManager;
import com.rather.parsoftables.Web.HandlerParsing.TableMetalsHandler;
import com.rather.parsoftables.Web.HandlerParsing.TableUniversalHandler;
import com.rather.parsoftables.Web.Parsing.TableMetalsParser;
import com.rather.parsoftables.Web.Parsing.TableUniversalParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.*;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class MainController {
    //---------------------------------------------------------------------------------------------------------
    private Scene scene;
    @FXML private AnchorPane MainAnchorPane;
    //---------------------------------------------------------------------------------------------------------
    private Label labelWithInfo = new Label("");
    @FXML private ScrollPane ScrollPaneWithInfo;
    private Label textErrors = new Label("");
    @FXML private ScrollPane ScrollPaneErrors;
    //---------------------------------------------------------------------------------------------------------
    // Параметры металлов.
    @FXML private TextField VolumeTextField;
    @FXML private TextField WeightTextField;
    //---------------------------------------------------
    // Параметры для парсинга таблиц.
    @FXML private TextField LinkTextField;
    @FXML private TextField cssQueryTextField;
    @FXML private TextField ColumnsTextField;
    @FXML private TextField RowsTextField;
    @FXML private TextField IndexTableTextField;
    //---------------------------------------------------
    @FXML private Button AcceptParametresButton;
    @FXML private Button ResetParametresButton;
    @FXML private Button SelectPathSavingFileButton;
    @FXML private CheckBox CheckBoxWayOfParsing;
    //---------------------------------------------------------------------------------------------------------
    // Параметры для сохранения файлов.
    @FXML private TextField pathOfSaveFileTextField;
    @FXML private TextField nameOfSaveFileTextField;
    private String pathOfSaveFile = "";
    //---------------------------------------------------
    @FXML private Button AcceptSaveFilesButton;
    @FXML private Button ResetSaveFilesButton;
    @FXML private CheckBox CheckBoxSaveFiles;
    //---------------------------------------------------------------------------------------------------------
    @FXML private AnchorPane AnchorPaneOfParametres;
    @FXML private AnchorPane AnchorPaneOfMetals;

    //---------------------------------------------------------------------------------------------------------
    // Текущие параметры для таблиц и металлов.
    private int currentWeight;
    private int currentVolume;
    private String currentLink;
    private String Xpath;
    private int currentColumns;
    private int currentRows;
    private int currentIndexTable;
    //---------------------------------------------------------------------------------------------------------
    // Элементы со всплывающего окна с информацией о копировании.
    private boolean isEnablePopupCopyTextInfoForLabelInfoAndErrors = false;
    // Получаем наше окно.
    private static Stage stage;
    private Label labelCopyTextInfoForLabelInfoAndErrors;
    private Label labelParseButtonPressed;
    private Label labelSaveButtonPressed;
    private Button buttonCopyTextInfoForLabelInfoAndErrors;
    private Button buttonParseButtonPressed;
    private Button buttonSaveButtonPressed;
    private HBox popupContentCopyTextInfoForLabelInfoAndErrors;
    private Popup popupCopyTextInfoForLabelInfoAndErrors;
    private Popup popupParseButtonPressed;
    private Popup popupSaveButtonPressed;
    private double anchorXPopupCopyTextInfoForLabelInfoAndErrors;
    private double anchorYPopupCopyTextInfoForLabelInfoAndErrors;

    private final int HeightPopupCopyTextInfoForLabelInfoAndErrors = 40;
    private final int WidthPopupCopyTextInfoForLabelInfoAndErrors = 120;
    //---------------------------------------------------------------------------------------------------------
    // Гиперссылки.
    @FXML private Hyperlink hyperLinkGithub;
    @FXML private ImageView imageGithubLink;
    @FXML private Hyperlink hyperLinkDiscord;
    @FXML private ImageView imageDiscordLink;
    @FXML private Hyperlink hyperLinkYoutube;
    @FXML private ImageView imageYoutubeLink;

    //---------------------------------------------------------------------------------------------------------

    // Настройка надписей в скролл панелях.
    private void setTextOfScrollPaneForLabelInfoAndErrors(Label label, ScrollPane scrollPane, double width, double height){
        label.setAlignment(Pos.TOP_LEFT);
        label.setWrapText(true);
        label.setPrefSize(width, height);
        label.setStyle("-fx-background-color: #595959; -fx-text-fill: #ffffff;");

        // Добавление обработчика событий
        label.setOnMouseClicked(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            clipboard.setContent(content);

            //stage = Application.getPrimaryStage();
            stage = (Stage) AcceptParametresButton.getScene().getWindow();
            Window window = AcceptParametresButton.getScene().getWindow();

            // Определить координаты верхнего левого угла главного окна
            double mainX = stage.getX();
            double mainY = stage.getY();

            // Определить размер главного окна
            double mainWidth = stage.getWidth();
            double mainHeight = stage.getHeight();

            // Определить размер экрана
            Rectangle2D screenBounds = Screen.getPrimary().getBounds();
            double screenWidth = screenBounds.getWidth();
            double screenHeight = screenBounds.getHeight();

            popupCopyTextInfoForLabelInfoAndErrors.xProperty().add(stage.xProperty().add(mainWidth));
            popupCopyTextInfoForLabelInfoAndErrors.yProperty().add(stage.yProperty());

            anchorXPopupCopyTextInfoForLabelInfoAndErrors = stage.getX() + (stage.getWidth() - HeightPopupCopyTextInfoForLabelInfoAndErrors*6 - 20);
            anchorYPopupCopyTextInfoForLabelInfoAndErrors = stage.getY() + (stage.getHeight() - WidthPopupCopyTextInfoForLabelInfoAndErrors/2 - 10);

            // Добавляем слушателя позиции главного окна
            window.xProperty().addListener((observable, oldValue, newValue) -> {
                double x = stage.getX() + (stage.getWidth() - HeightPopupCopyTextInfoForLabelInfoAndErrors*6 - 20);
                double y = stage.getY() + (stage.getHeight() - WidthPopupCopyTextInfoForLabelInfoAndErrors/2 - 10);
                popupCopyTextInfoForLabelInfoAndErrors.setX(x);
                popupCopyTextInfoForLabelInfoAndErrors.setY(y);
            });

            // Обрабатываем нажатие на кнопку
            // Добавляем действие кнопке для скрытия окна.
            buttonCopyTextInfoForLabelInfoAndErrors.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    popupCopyTextInfoForLabelInfoAndErrors.hide();
                    popupParseButtonPressed.hide();
                    popupSaveButtonPressed.hide();
                    isEnablePopupCopyTextInfoForLabelInfoAndErrors = false;
                }
            });

            // Проверка на существование окна, при его активности повторно оно создаваться не будет.
            if(!isEnablePopupCopyTextInfoForLabelInfoAndErrors) {
                isEnablePopupCopyTextInfoForLabelInfoAndErrors = true;

                // Работа над сплывающим окном с информацией о том, что запарсенный текст был скопирован.
                try {
                    // Показываем всплывающее окно
                    popupCopyTextInfoForLabelInfoAndErrors.show(stage, anchorXPopupCopyTextInfoForLabelInfoAndErrors, anchorYPopupCopyTextInfoForLabelInfoAndErrors);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                isEnablePopupCopyTextInfoForLabelInfoAndErrors = true;
                popupCopyTextInfoForLabelInfoAndErrors.hide();
                popupParseButtonPressed.hide();
                popupSaveButtonPressed.hide();
                popupCopyTextInfoForLabelInfoAndErrors.show(stage, anchorXPopupCopyTextInfoForLabelInfoAndErrors, anchorYPopupCopyTextInfoForLabelInfoAndErrors);
            }
        });

        scrollPane.setContent(label);
        scrollPane.setStyle("-fx-background-color: #595959; -fx-border-color: #ffffff;");
    }

    private void setPopupsForParsingSaving(String parseOrSave){
        stage = (Stage) AcceptParametresButton.getScene().getWindow();
        Window window = AcceptParametresButton.getScene().getWindow();

        // Определить координаты верхнего левого угла главного окна
        double mainX = stage.getX();
        double mainY = stage.getY();

        // Определить размер главного окна
        double mainWidth = stage.getWidth();
        double mainHeight = stage.getHeight();

        // Определить размер экрана
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        if (parseOrSave.equals("parse")){
            popupParseButtonPressed.xProperty().add(stage.xProperty().add(mainWidth));
            popupParseButtonPressed.yProperty().add(stage.yProperty());

            anchorXPopupCopyTextInfoForLabelInfoAndErrors = stage.getX() + (stage.getWidth() - HeightPopupCopyTextInfoForLabelInfoAndErrors*6 - 20);
            anchorYPopupCopyTextInfoForLabelInfoAndErrors = stage.getY() + (stage.getHeight() - WidthPopupCopyTextInfoForLabelInfoAndErrors/2 - 10);

            // Добавляем слушателя позиции главного окна
            window.xProperty().addListener((observable, oldValue, newValue) -> {
                double x = stage.getX() + (stage.getWidth() - HeightPopupCopyTextInfoForLabelInfoAndErrors*6 - 20);
                double y = stage.getY() + (stage.getHeight() - WidthPopupCopyTextInfoForLabelInfoAndErrors/2 - 10);
                popupParseButtonPressed.setX(x);
                popupParseButtonPressed.setY(y);
            });

            // Обрабатываем нажатие на кнопку
            // Добавляем действие кнопке для скрытия окна.
            buttonParseButtonPressed.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    popupCopyTextInfoForLabelInfoAndErrors.hide();
                    popupParseButtonPressed.hide();
                    popupSaveButtonPressed.hide();
                    isEnablePopupCopyTextInfoForLabelInfoAndErrors = false;
                }
            });

            // Проверка на существование окна, при его активности повторно оно создаваться не будет.
            if(!isEnablePopupCopyTextInfoForLabelInfoAndErrors) {
                isEnablePopupCopyTextInfoForLabelInfoAndErrors = true;

                // Работа над сплывающим окном с информацией о том, что запарсенный текст был скопирован.
                try {
                    // Показываем всплывающее окно
                    popupParseButtonPressed.show(stage, anchorXPopupCopyTextInfoForLabelInfoAndErrors, anchorYPopupCopyTextInfoForLabelInfoAndErrors);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                isEnablePopupCopyTextInfoForLabelInfoAndErrors = true;
                popupCopyTextInfoForLabelInfoAndErrors.hide();
                popupParseButtonPressed.hide();
                popupSaveButtonPressed.hide();
                popupParseButtonPressed.show(stage, anchorXPopupCopyTextInfoForLabelInfoAndErrors, anchorYPopupCopyTextInfoForLabelInfoAndErrors);
            }
        } else {
            popupSaveButtonPressed.xProperty().add(stage.xProperty().add(mainWidth));
            popupSaveButtonPressed.yProperty().add(stage.yProperty());

            anchorXPopupCopyTextInfoForLabelInfoAndErrors = stage.getX() + (stage.getWidth() - HeightPopupCopyTextInfoForLabelInfoAndErrors*6 - 20);
            anchorYPopupCopyTextInfoForLabelInfoAndErrors = stage.getY() + (stage.getHeight() - WidthPopupCopyTextInfoForLabelInfoAndErrors/2 - 10);

            // Добавляем слушателя позиции главного окна
            window.xProperty().addListener((observable, oldValue, newValue) -> {
                double x = stage.getX() + (stage.getWidth() - HeightPopupCopyTextInfoForLabelInfoAndErrors*6 - 20);
                double y = stage.getY() + (stage.getHeight() - WidthPopupCopyTextInfoForLabelInfoAndErrors/2 - 10);
                popupSaveButtonPressed.setX(x);
                popupSaveButtonPressed.setY(y);
            });

            // Обрабатываем нажатие на кнопку
            // Добавляем действие кнопке для скрытия окна.
            buttonSaveButtonPressed.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    popupCopyTextInfoForLabelInfoAndErrors.hide();
                    popupParseButtonPressed.hide();
                    popupSaveButtonPressed.hide();
                    isEnablePopupCopyTextInfoForLabelInfoAndErrors = false;
                }
            });

            // Проверка на существование окна, при его активности повторно оно создаваться не будет.
            if(!isEnablePopupCopyTextInfoForLabelInfoAndErrors) {
                isEnablePopupCopyTextInfoForLabelInfoAndErrors = true;

                // Работа над сплывающим окном с информацией о том, что запарсенный текст был скопирован.
                try {
                    // Показываем всплывающее окно
                    popupSaveButtonPressed.show(stage, anchorXPopupCopyTextInfoForLabelInfoAndErrors, anchorYPopupCopyTextInfoForLabelInfoAndErrors);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                isEnablePopupCopyTextInfoForLabelInfoAndErrors = true;
                popupCopyTextInfoForLabelInfoAndErrors.hide();
                popupParseButtonPressed.hide();
                popupSaveButtonPressed.hide();
                popupSaveButtonPressed.show(stage, anchorXPopupCopyTextInfoForLabelInfoAndErrors, anchorYPopupCopyTextInfoForLabelInfoAndErrors);
            }
        }
    }

    // Вывод ошибки в надпись при не правильно заполненых полях.
    private void sendErrorMessageAboutIncorrectInputValues(String valueWithProblem, Exception exception){
        textErrors.setText(
                textErrors.getText()
                        + "Input of " + valueWithProblem +" is incorrect!" + "\n"
                        + exception + "\n\n"
        );
    }

    // Вывод ошибки в надпись при не правильно заполненых полях.
    private void sendErrorMessageAboutRunTime(String runtimeProblem, Exception exception){
        textErrors.setText(
                textErrors.getText()
                        + runtimeProblem + ": " + "\n"
                        + exception + "\n\n"
        );
    }

    private static void setSameStage(){
        stage = Application.getPrimaryStage();
    }

    @FXML
    public void initialize() {

        //-------------------------------------{
        // Получение сцены.
        scene = AcceptParametresButton.getScene();
        //-------------------------------------}

        //-------------------------------------{
        // Настройка и добавление элементов со всплывающего окна с информацией о копировании.

        // Окно о начавшемся парсинге
        // Настроиваем надпись.
        labelParseButtonPressed = new Label("Parsing started");
        labelParseButtonPressed.setStyle("-fx-background-color: #595959; -fx-text-fill: #ffffff;");
        labelParseButtonPressed.setWrapText(true);

        // Настроиваем кнопку.
        buttonParseButtonPressed = new Button("Close");
        buttonParseButtonPressed.setStyle("-fx-background-color: #404040; -fx-background-radius: 5; -fx-border-radius: 3; -fx-border-color: #FFFFFF;");
        buttonParseButtonPressed.setTextFill(Paint.valueOf("#ebebeb"));

        // Создаем контент для всплывающего окна
        popupContentCopyTextInfoForLabelInfoAndErrors = new HBox();
        popupContentCopyTextInfoForLabelInfoAndErrors.setAlignment(Pos.CENTER);
        popupContentCopyTextInfoForLabelInfoAndErrors.getChildren().add(labelParseButtonPressed);
        popupContentCopyTextInfoForLabelInfoAndErrors.getChildren().add(buttonParseButtonPressed);
        popupContentCopyTextInfoForLabelInfoAndErrors.setStyle("-fx-background-color: #595959; -fx-border-color: #FFFFFF; -fx-padding: 10px; -fx-background-radius: 5; -fx-border-radius: 3;");
        popupContentCopyTextInfoForLabelInfoAndErrors.setSpacing(10);

        // Создаем всплывающее окно
        popupParseButtonPressed = new Popup();
        popupParseButtonPressed.getContent().add(popupContentCopyTextInfoForLabelInfoAndErrors);
        popupParseButtonPressed.setHeight(HeightPopupCopyTextInfoForLabelInfoAndErrors);
        popupParseButtonPressed.setWidth(WidthPopupCopyTextInfoForLabelInfoAndErrors);


        // Окно о сохраненном файле
        // Настроиваем надпись.
        labelSaveButtonPressed = new Label("The file was saved");
        labelSaveButtonPressed.setStyle("-fx-background-color: #595959; -fx-text-fill: #ffffff;");
        labelSaveButtonPressed.setWrapText(true);

        // Настроиваем кнопку.
        buttonSaveButtonPressed = new Button("Close");
        buttonSaveButtonPressed.setStyle("-fx-background-color: #404040; -fx-background-radius: 5; -fx-border-radius: 3; -fx-border-color: #FFFFFF;");
        buttonSaveButtonPressed.setTextFill(Paint.valueOf("#ebebeb"));

        // Создаем контент для всплывающего окна
        popupContentCopyTextInfoForLabelInfoAndErrors = new HBox();
        popupContentCopyTextInfoForLabelInfoAndErrors.setAlignment(Pos.CENTER);
        popupContentCopyTextInfoForLabelInfoAndErrors.getChildren().add(labelSaveButtonPressed);
        popupContentCopyTextInfoForLabelInfoAndErrors.getChildren().add(buttonSaveButtonPressed);
        popupContentCopyTextInfoForLabelInfoAndErrors.setStyle("-fx-background-color: #595959; -fx-border-color: #FFFFFF; -fx-padding: 10px; -fx-background-radius: 5; -fx-border-radius: 3;");
        popupContentCopyTextInfoForLabelInfoAndErrors.setSpacing(10);

        // Создаем всплывающее окно
        popupSaveButtonPressed = new Popup();
        popupSaveButtonPressed.getContent().add(popupContentCopyTextInfoForLabelInfoAndErrors);
        popupSaveButtonPressed.setHeight(HeightPopupCopyTextInfoForLabelInfoAndErrors);
        popupSaveButtonPressed.setWidth(WidthPopupCopyTextInfoForLabelInfoAndErrors);


        // Окно о скопированном тексте
        // Настроиваем надпись.
        labelCopyTextInfoForLabelInfoAndErrors = new Label("The label text has been copied");
        labelCopyTextInfoForLabelInfoAndErrors.setStyle("-fx-background-color: #595959; -fx-text-fill: #ffffff;");
        labelCopyTextInfoForLabelInfoAndErrors.setWrapText(true);

        // Настроиваем кнопку.
        buttonCopyTextInfoForLabelInfoAndErrors = new Button("Close");
        buttonCopyTextInfoForLabelInfoAndErrors.setStyle("-fx-background-color: #404040; -fx-background-radius: 5; -fx-border-radius: 3; -fx-border-color: #FFFFFF;");
        buttonCopyTextInfoForLabelInfoAndErrors.setTextFill(Paint.valueOf("#ebebeb"));

        // Создаем контент для всплывающего окна
        popupContentCopyTextInfoForLabelInfoAndErrors = new HBox();
        popupContentCopyTextInfoForLabelInfoAndErrors.setAlignment(Pos.CENTER);
        popupContentCopyTextInfoForLabelInfoAndErrors.getChildren().add(labelCopyTextInfoForLabelInfoAndErrors);
        popupContentCopyTextInfoForLabelInfoAndErrors.getChildren().add(buttonCopyTextInfoForLabelInfoAndErrors);
        popupContentCopyTextInfoForLabelInfoAndErrors.setStyle("-fx-background-color: #595959; -fx-border-color: #FFFFFF; -fx-padding: 10px; -fx-background-radius: 5; -fx-border-radius: 3;");
        popupContentCopyTextInfoForLabelInfoAndErrors.setSpacing(10);

        // Создаем всплывающее окно
        popupCopyTextInfoForLabelInfoAndErrors = new Popup();
        popupCopyTextInfoForLabelInfoAndErrors.getContent().add(popupContentCopyTextInfoForLabelInfoAndErrors);
        popupCopyTextInfoForLabelInfoAndErrors.setHeight(HeightPopupCopyTextInfoForLabelInfoAndErrors);
        popupCopyTextInfoForLabelInfoAndErrors.setWidth(WidthPopupCopyTextInfoForLabelInfoAndErrors);

        //-------------------------------------}

        //-------------------------------------{
        // Гиперссылки.
        Application a = new Application() {
            @Override
            public void start(Stage stage) throws IOException {
            }
        };

        // Получение пути для ссылки на изображения
        String currentDirPath = System.getProperty("user.dir");
        String path = new File(currentDirPath).getParent().replace("\\", "/") + "/Newbie_TableParser-Github/src/main/resources/images/";

        try{
            imageGithubLink.setImage(new Image(path + "GitHub.png"));
            imageDiscordLink.setImage(new Image(path + "discord.png"));
            imageYoutubeLink.setImage(new Image(path + "youtube.png"));
        } catch (Exception e){
            try{
                imageGithubLink.setImage(new Image("https://cdn.wikimg.net/en/splatoonwiki/images/thumb/8/88/GitHub_Icon.svg/1200px-GitHub_Icon.svg.png"));
                imageDiscordLink.setImage(new Image("https://freelogopng.com/images/all_img/1691730813discord-icon-png.png"));
                imageYoutubeLink.setImage(new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a0/YouTube_social_red_circle_%282017%29.svg/2048px-YouTube_social_red_circle_%282017%29.svg.png"));
            } catch (Exception e2){}
        }

        // Настройка ссылку на github
        hyperLinkGithub.setText("Github");
        hyperLinkGithub.setOnAction(e -> {
            a.getHostServices().showDocument("https://github.com/NewMrPotato");
        });

        // Настройка ссылку на discord
        hyperLinkDiscord.setText("Discord");
        hyperLinkDiscord.setOnAction(e -> {
            a.getHostServices().showDocument("https://discord.gg/s5Db7YKn");
        });

        // Настройка ссылку на youtube
        hyperLinkYoutube.setText("Youtube");
        hyperLinkYoutube.setOnAction(e -> {
            a.getHostServices().showDocument("https://www.youtube.com/channel/UCaxlgLlde3I5BTGNY0OLhgA");
        });

        //-------------------------------------}

        //-------------------------------------{
        // Настройка и добавление надписи с информацией.
        setTextOfScrollPaneForLabelInfoAndErrors(labelWithInfo, ScrollPaneWithInfo, 470, 4000);
        //-------------------------------------}

        //-------------------------------------{
        // Настройка и добавление надписи с ошибками.
        setTextOfScrollPaneForLabelInfoAndErrors(textErrors, ScrollPaneErrors, 650, 1000);
        //-------------------------------------}

        AcceptParametresButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setPopupsForParsingSaving("parse");

                if (CheckBoxWayOfParsing.isSelected()) {
                    //-------------------------------------{
                    // Проверка на правильный ввод полей с весом и объемом.
                    try { // weight.
                        currentWeight = Integer.parseInt(WeightTextField.getText());
                    } catch (Exception exception) {
                        sendErrorMessageAboutIncorrectInputValues("weight", exception);
                    }
                    try { // volume.
                        currentVolume = Integer.parseInt(VolumeTextField.getText());
                    } catch (Exception exception) {
                        sendErrorMessageAboutIncorrectInputValues("volume", exception);
                    }
                    //-------------------------------------}

                    //-------------------------------------{
                    // Парсинг частного случая таблицы, таблицы с металлами.
                    TableMetalsHandler tableMetalsHandler = new TableMetalsHandler();
                    TableMetalsParser tableMetalsParser = new TableMetalsParser();
                    String referenceForSiteWithInfoOfMetals = "https://www.galakmet.ru/directory/density/";
                    String XpathM = "";

                    Elements metals = tableMetalsParser.parse(referenceForSiteWithInfoOfMetals, XpathM, 0, 0, 0);
                    labelWithInfo.setText(tableMetalsHandler.handle(metals, 0, 0 ));
                    //-------------------------------------}
                }else {
                    //-------------------------------------{
                    // Проверка на правильный ввода параметров парсинга.
                    try { // rows.
                        currentRows = Integer.parseInt(RowsTextField.getText());
                    } catch (Exception exception) {
                        sendErrorMessageAboutIncorrectInputValues("rows", exception);
                    }
                    try { // columns.
                        currentColumns = Integer.parseInt(ColumnsTextField.getText());
                    } catch (Exception exception) {
                        sendErrorMessageAboutIncorrectInputValues("columns", exception);
                    }
                    try { // link.
                        currentLink = LinkTextField.getText();
                    } catch (Exception exception) {
                        sendErrorMessageAboutIncorrectInputValues("link", exception);
                    }
                    try { // Xpath.
                        Xpath = cssQueryTextField.getText();
                    } catch (Exception exception) {
                        sendErrorMessageAboutIncorrectInputValues("cssQuery", exception);
                    }
                    try { // indexTable.
                        currentIndexTable = Integer.parseInt(IndexTableTextField.getText().replace(" ", ""));
                    } catch (Exception exception) {
                        sendErrorMessageAboutIncorrectInputValues("indexTable", exception);
                    }
                    //-------------------------------------}

                    //-------------------------------------{
                    // Универсальный парсинг таблиц.
                    TableUniversalParser tableUniversalParser = new TableUniversalParser();
                    TableUniversalHandler tableUniversalHandler = new TableUniversalHandler();

                    try {
                        labelWithInfo.setText(tableUniversalHandler
                                .handle(tableUniversalParser
                                .parse(currentLink, Xpath, currentRows, currentColumns, currentIndexTable), currentRows, currentColumns ));
                    }catch (NullPointerException nullPointerException){
                        sendErrorMessageAboutRunTime("The table could not be found, or incorrect parameter input", nullPointerException);
                    }catch (Exception exception){
                        sendErrorMessageAboutRunTime("An error occurred in the operation", exception);
                    }
                    //-------------------------------------}
                }
            }
        });

        //-------------------------------------{
        // Выбор директории для сохранения таблицы.
        SelectPathSavingFileButton.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select directory for save the table");

            try {
                File directory = directoryChooser.showDialog(MainAnchorPane.getScene().getWindow());
                pathOfSaveFile = directory.getPath() + "\\";
                pathOfSaveFileTextField.setText(pathOfSaveFile);
            } catch (Exception exception){
                sendErrorMessageAboutRunTime("The folder is not defined", exception);
            }
        });
        //-------------------------------------}

        //-------------------------------------{
        // Сохранение таблицы в формате xml с помощью ExcelManager, используя введенныу путь и имя файла.
        AcceptSaveFilesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setPopupsForParsingSaving("123");

                ExcelManager manager = new ExcelManager();

                if (CheckBoxWayOfParsing.isSelected()) {
                    String referenceForSiteWithInfoOfMetals = "https://www.galakmet.ru/directory/density/";
                    String[] cssQueryForTable = {"cellspacing=0", "cellpadding=5"};

                    try {
                        manager.convertTableToExcel(pathOfSaveFileTextField.getText(), nameOfSaveFileTextField.getText(),
                                referenceForSiteWithInfoOfMetals, Xpath, 174, 4, 0);
                    } catch (Exception exception) {
                        sendErrorMessageAboutRunTime("An error occurred in the operation", exception);
                    }
                }else {

                    try {
                        manager.convertTableToExcel(pathOfSaveFile, nameOfSaveFileTextField.getText(),
                                currentLink, Xpath, currentRows, currentColumns, currentIndexTable);
                    } catch (Exception exception) {
                        sendErrorMessageAboutRunTime("An error occurred while saving an excel file", exception);
                    }
                }
            }
        });//-------------------------------------}

        //-------------------------------------{
        // Очистка информации с основной надписи и с полей с вводным текстом.
        ResetParametresButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                labelWithInfo.setText("");
                textErrors.setText("");

                if (CheckBoxWayOfParsing.isSelected()){
                    WeightTextField.setText("");
                    currentWeight = 0;
                    VolumeTextField.setText("");
                    currentVolume = 0;
                } else{
                    LinkTextField.setText("");
                    currentLink = "";
                    cssQueryTextField.setText("");
                    Xpath = null;
                    RowsTextField.setText("");
                    currentRows = 0;
                    ColumnsTextField.setText("");
                    currentColumns = 0;
                    IndexTableTextField.setText("");
                    currentIndexTable = 0;
                }
            }
        });
        //-------------------------------------}

        //-------------------------------------{
        // Очистка информации о сохраняемом файле с полей ввода текста.
        ResetSaveFilesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pathOfSaveFileTextField.setText("");
                nameOfSaveFileTextField.setText("");

            }
        });
        //-------------------------------------}

        CheckBoxWayOfParsing.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //-------------------------------------{
                if(CheckBoxWayOfParsing.isSelected()) {
                    AnchorPaneOfMetals.setVisible(true);
                    AnchorPaneOfParametres.setVisible(false);
                } else {
                    AnchorPaneOfMetals.setVisible(false);
                    AnchorPaneOfParametres.setVisible(true);
                }
                //-------------------------------------}
            }
        });

        CheckBoxSaveFiles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //-------------------------------------{
                if(CheckBoxSaveFiles.isSelected()) {
                    pathOfSaveFileTextField.setDisable(false);
                    nameOfSaveFileTextField.setDisable(false);
                    AcceptSaveFilesButton.setDisable(false);
                    ResetSaveFilesButton.setDisable(false);
                    SelectPathSavingFileButton.setDisable(false);
                } else {
                    pathOfSaveFileTextField.setDisable(true);
                    nameOfSaveFileTextField.setDisable(true);
                    AcceptSaveFilesButton.setDisable(true);
                    ResetSaveFilesButton.setDisable(true);
                    SelectPathSavingFileButton.setDisable(true);
                }
                //-------------------------------------}
            }
        });
    }
}

