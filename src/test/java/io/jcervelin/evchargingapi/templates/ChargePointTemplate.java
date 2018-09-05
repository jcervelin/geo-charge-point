package io.jcervelin.evchargingapi.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.jcervelin.evchargingapi.domains.ChargePoint;
import org.springframework.data.geo.Point;

public class ChargePointTemplate implements TemplateLoader {

    public static final String CHELTENHAM_CHASE_HOTEL = "Cheltenham Chase Hotel";
    public static final String POOLE_CIVIC_CENTRE_SURFACE_CAR_PARK = "Poole Civic Centre Surface Car Park";
    public static final String CROSBY_LAKESIDE_ADVENTURE_CENTRE = "Crosby Lakeside Adventure Centre";
    public static final String WEBBS_OF_WYCHBOLD_RAPID_CHARGER = "Webbs of Wychbold Rapid Charger";
    public static final String LONGWELL_GREEN_LEISURE_CENTRE = "Longwell Green Leisure Centre";
    public static final String BIRKDALE_PRIMARY_SCHOOL = "Birkdale Primary School";
    public static final String RC_BOTHWELL = "RC Bothwell";
    public static final String RC_ROWNHAMS = "RC Rownhams North";
    public static final String ASDA_CHARLTON = "Asda Charlton";
    public static final String ASDA_GREENHITHE = "Asda Greenhithe";

    @Override
    public void load() {
        Fixture.of(ChargePoint.class).addTemplate(RC_BOTHWELL, new Rule() {{
            add("chargeDeviceID", "8b96464d7efe28c47cd6d9b23fb04e17");
            add("name", "RC Bothwell");
            add("location", new Point(55.814239,-4.062318));
        }});

        Fixture.of(ChargePoint.class).addTemplate(RC_ROWNHAMS, new Rule() {{
            add("chargeDeviceID", "cfa37ea299061248adb9c9b895ecfd85");
            add("name", "RC Rownhams North");
            add("location", new Point(50.958239,-1.448169));
        }});

        Fixture.of(ChargePoint.class).addTemplate(ASDA_CHARLTON, new Rule() {{
            add("chargeDeviceID", "47f1adc470baaf1874d79dc4144af95f");
            add("name", "Asda Charlton");
            add("location", new Point(51.488779,0.022515));
        }});

        Fixture.of(ChargePoint.class).addTemplate(ASDA_GREENHITHE, new Rule() {{
            add("chargeDeviceID", "0a3cb8d6a1d544b17d920ff2d36b8c18");
            add("name", "Asda Greenhithe");
            add("location", new Point(51.452537,0.275216));
        }});

        Fixture.of(ChargePoint.class).addTemplate(CHELTENHAM_CHASE_HOTEL, new Rule() {{
            add("chargeDeviceID", "1404c18253facdb94b99cafc3cf38fbb");
            add("name", "Cheltenham Chase Hotel");
            add("location", new Point(51.843692,-2.146016));
        }});

        Fixture.of(ChargePoint.class).addTemplate(POOLE_CIVIC_CENTRE_SURFACE_CAR_PARK, new Rule() {{
            add("chargeDeviceID", "c71bce52ccb738139dabb8d50d1e1de7");
            add("name", "Poole Civic Centre Surface Car Park");
            add("location", new Point(50.72306,-1.96128));
        }});

        Fixture.of(ChargePoint.class).addTemplate(CROSBY_LAKESIDE_ADVENTURE_CENTRE, new Rule() {{
            add("chargeDeviceID", "da0960648502d422c8412a9b1c5bb73f");
            add("name", "Crosby Lakeside Adventure Centre");
            add("location", new Point(53.47036,-3.028764));
        }});

        Fixture.of(ChargePoint.class).addTemplate(WEBBS_OF_WYCHBOLD_RAPID_CHARGER, new Rule() {{
            add("chargeDeviceID", "c2a7ef718be41c2df3cdf78801ff1490");
            add("name", "Webbs of Wychbold Rapid Charger");
            add("location", new Point(52.302563,-2.105456));
        }});

        Fixture.of(ChargePoint.class).addTemplate(LONGWELL_GREEN_LEISURE_CENTRE, new Rule() {{
            add("chargeDeviceID", "a999c0cab150490ffefb7ce9ec889830");
            add("name", "Longwell Green Leisure Centre");
            add("location", new Point(51.449065,-2.500966));
        }});

        Fixture.of(ChargePoint.class).addTemplate(BIRKDALE_PRIMARY_SCHOOL, new Rule() {{
            add("chargeDeviceID", "9c20589c29266540045a9cbfdf12c64a");
            add("name", "Birkdale Primary School");
            add("location", new Point(53.631211,-3.005988));
        }});

    }
}
