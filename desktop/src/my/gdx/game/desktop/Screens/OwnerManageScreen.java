package my.gdx.game.desktop.Screens;

import java.sql.SQLException;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import my.gdx.game.desktop.Actor.MyActor;
import my.gdx.game.desktop.User.Owner;
import my.gdx.game.desktop.User.OwnerManagement;
import my.gdx.game.desktop.Utils.Asset;
import my.gdx.game.desktop.Utils.Button;
import my.gdx.game.desktop.Utils.Point;
import my.gdx.game.desktop.Utils.Size;
import my.gdx.game.desktop.Utils.StaticSupport;

public class OwnerManageScreen implements ScreenInterface, Screen {
    private Stage stage;
    private Group backGround;
    // private Label lblId;
    private Game game;
    private TextField lookUp;
    private Table table = new Table();
    private Table ttable = new Table();
    private Owner temp;

    public OwnerManageScreen(Game g) {
        stage = new Stage(new ScreenViewport());
        this.game = g;
        Gdx.input.setInputProcessor(stage);
        setBackground();
        setItem();
    }

    private void addLblUserWin() {
        Label lblUser = new Label("UserName: " + Asset.userName, new LabelStyle(Asset.font, Color.RED));
        Label lblWin = new Label("WIN: " + Asset.win, new LabelStyle(Asset.font, Color.RED));
        lblUser.setFontScale(0.75f);
        lblWin.setFontScale(0.75f);
        lblUser.setPosition(20, stage.getHeight() - 30);
        lblWin.setPosition(20, stage.getHeight() - 60);
        backGround.addActor(lblUser);
        backGround.addActor(lblWin);
    }

    private void addBtnHome() {
        final Button btnHome;
        Size btnHomeSize = new Size(75, 75);
        btnHome = new Button("btnBack.png", new Point(stage.getWidth() - btnHomeSize.getWidth() / 2 - 20,
                stage.getHeight() - btnHomeSize.getHeight() / 2 - 20), btnHomeSize);
        backGround.addActor(btnHome);

        btnHome.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnHome.normalContain(btnHome.getX() + x, btnHome.getY() + y))
                    game.setScreen(new ManagerScreen(game));
            }
        });
    }

    private void addBtnLookUp() {
        Size sizeBtn = new Size(31, 31);
        final Button btnLookUp = new Button("btnLookUp.png",
                new Point(lookUp.getX() + lookUp.getWidth() + sizeBtn.getWidth() + 10,
                        lookUp.getY() + sizeBtn.getHeight() / 2),
                sizeBtn);
        backGround.addActor(btnLookUp);
        btnLookUp.removeListener(btnLookUp.getListeners().get(0));
        btnLookUp.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btnLookUp.addAction(Actions.scaleTo(0.75f, 0.75f));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnLookUp.normalContain(btnLookUp.getX() + x, btnLookUp.getY() + y))
                    try {
                        setListLookUp(lookUp.getText());
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                btnLookUp.addAction(Actions.scaleTo(1, 1));
            }
        });
    }

    private void addTxtFldLookUp() {
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        lookUp = new TextField("", skin);
        Size sizeTxt = new Size(stage.getWidth() / 4, 30);
        lookUp.setSize(sizeTxt.getWidth(), sizeTxt.getHeight());
        lookUp.setPosition(stage.getWidth() / 2 - sizeTxt.getWidth() / 2, stage.getWidth() - 45);
        backGround.addActor(lookUp);
    }

    private void setListAll() throws SQLException {
        Asset.getAllOwner();
        setListUser(OwnerManagement.arrOwner);
    }

    private void setListLookUp(String txt) throws SQLException {
        Asset.getAllOwner();
        ArrayList<Owner> listLook = new ArrayList<>();
        int textInt = StaticSupport.isDigit(txt) ? Integer.parseInt(txt) : 0;
        for (Owner i : OwnerManagement.arrOwner)
            if (i.getIdPlayer() == textInt)
                listLook.add(0, i);
            else if (i.getIdCaptain().contains(txt))
                listLook.add(i);
        setListUser(listLook);
    }

    private void setListUser(ArrayList<Owner> arrOwner) {
        backGround.removeActor(table);
        backGround.removeActor(ttable);
        table.clear();
        ttable.clear();
        table = new Table();
        table.setBounds(0, 0, backGround.getWidth() - 200, backGround.getHeight() - 250);
        Image img = new Image(new Texture("board.png"));
        img.setBounds(0, 0, backGround.getWidth(), backGround.getHeight() - 100);
        Label lblIdP = new Label("idPlayer", new LabelStyle(Asset.font, Color.ORANGE));
        Label lblIdCaptain = new Label("idCaptain", new LabelStyle(Asset.font, Color.ORANGE));
        Label lblDayOwn = new Label("dayown", new LabelStyle(Asset.font, Color.ORANGE));
        Label lblStatusUse = new Label("statususe", new LabelStyle(Asset.font, Color.ORANGE));
        Label lblRemove = new Label("REMOVE", new LabelStyle(Asset.font, Color.ORANGE));
        table.row().fill().expand();
        table.add(lblIdP);
        table.add(lblIdCaptain);
        table.add(lblDayOwn);
        table.add(lblStatusUse);
        table.add(lblRemove);
        for (int i = 0; i < arrOwner.size(); i++) {
            table.row().fill().expand();
            Color color = Color.WHITE;
            Label idP = new Label(arrOwner.get(i).getIdPlayer() + "", new LabelStyle(Asset.font, color));
            Label idCaptain = new Label(arrOwner.get(i).getIdCaptain().replace(" ", ""),
                    new LabelStyle(Asset.font, color));
            Label dayOwn = new Label(arrOwner.get(i).getDayOwn(), new LabelStyle(Asset.font, color));
            Label statusUse = new Label(arrOwner.get(i).getStatusUse() + "", new LabelStyle(Asset.font, color));
            final Button btnRemove = new Button("btnDelete.png");
            btnRemove.setWidth(30);
            btnRemove.setHeight(30);
            final ArrayList<Owner> AR = arrOwner;
            final Owner cur = AR.get(i);
            btnRemove.addListener(new ClickListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    // TODO Auto-generated method stub
                    if (btnRemove.normalContain(btnRemove.getX() + x, btnRemove.getY() + y)) {
                        System.out.println("ok");
                        temp = cur;
                        addBoardRemove(AR);
                    }
                }
            });

            table.add(idP);
            table.add(idCaptain);
            table.add(dayOwn);
            table.add(statusUse);
            table.add(btnRemove).padLeft(-100);
        }
        if (arrOwner.size() < 8)
            for (int i = 0; i < 8 - arrOwner.size(); i++) {
                table.row().fill().expand();
                Label space = new Label("", new LabelStyle(Asset.font, Color.RED));
                table.add(space);
            }
        final ScrollPane scroller = new ScrollPane(table);
        ttable.add(scroller).width(stage.getWidth()).height(img.getHeight() - 200);
        ttable.setPosition(440, 390);
        scroller.setScrollingDisabled(true, false);
        backGround.addActor(img);
        backGround.addActor(ttable);
    }

    private void addBoardRemove(final ArrayList<Owner> aR) {
        final MyActor board = new MyActor("back.png", new Point(stage.getWidth() / 2, stage.getHeight() + 400),
                new Size(600, 400));
        backGround.addActor(board);
        board.addAction(Actions.moveTo(board.getX(), stage.getHeight() / 2 - board.getHeight() / 2, 0.2f));
        final Label quest = new Label("DO YOU WANT TO DELETE IT ?", new LabelStyle(Asset.font, Color.YELLOW));
        quest.setPosition(board.getX() + 140, board.getY() + 350);
        backGround.addActor(quest);
        quest.addAction(Actions.moveTo(quest.getX(), stage.getHeight() / 2 + 100, 0.2f));

        final Button btnYes = new Button("btnYes.png", new Point(stage.getWidth() / 2 - 100, board.getY() - 150),
                new Size(80, 80));
        backGround.addActor(btnYes);
        btnYes.addAction(Actions.moveTo(btnYes.getX(), stage.getHeight() / 2 - board.getHeight() / 4, 0.2f));

        final Button btnNo = new Button("btnNo.png", new Point(stage.getWidth() / 2 + 100, board.getY() - 150),
                new Size(80, 80));
        backGround.addActor(btnNo);
        btnNo.addAction(Actions.moveTo(btnNo.getX(), stage.getHeight() / 2 - board.getHeight() / 4, 0.2f));

        btnYes.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnYes.normalContain(btnYes.getX() + x, btnYes.getY() + y)) {
                    OwnerManagement.removeOwner(temp);
                    String query = "DELETE FROM ownerCaptain WHERE idPlayer = " + temp.getIdPlayer()
                            + " AND idCaptain = '" + temp.getIdCaptain() + "'";
                    try {
                        Asset.setData(query);
                        board.remove();
                        quest.remove();
                        btnYes.remove();
                        btnNo.remove();
                        setListAll();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        btnNo.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (btnNo.normalContain(btnNo.getX() + x, btnNo.getY() + y)) {
                    board.remove();
                    quest.remove();
                    btnYes.remove();
                    btnNo.remove();
                    try {
                        setListAll();
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }

    @Override
    public void render(float delta) {
        // TODO Auto-generated method stub
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

    @Override
    public void animateStarting() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setBackground() {
        // TODO Auto-generated method stub
        backGround = new Group();
        backGround.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backGround);
        Image img = new Image(Asset.backGround);
        img.setBounds(0, 0, backGround.getWidth(), backGround.getHeight());
        backGround.addActor(img);
    }

    @Override
    public void setItem() {
        // TODO Auto-generated method stub
        try {
            setListAll();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        addTxtFldLookUp();
        addBtnLookUp();
        addBtnHome();
        addLblUserWin();
    }

}
