package com.plasmideditor.rocket.genbank.domains;

import org.biojava.nbio.core.sequence.Strand;
import org.biojava.nbio.core.sequence.location.SimplePoint;
import org.biojava.nbio.core.sequence.location.template.AbstractLocation;
import org.biojava.nbio.core.sequence.location.template.Location;
import org.biojava.nbio.core.sequence.location.template.Point;

import java.util.ArrayList;

public class RaketaLocation extends AbstractLocation {

    public RaketaLocation(int start, int end) {
        this(new SimplePoint(start), new SimplePoint(end), Strand.POSITIVE);
    }

    public RaketaLocation(Point start, Point end, Strand strand) {
        super(start, end, strand, false, false, new ArrayList<Location>());
    }
}
