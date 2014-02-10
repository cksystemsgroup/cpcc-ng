// ae \u00e4, oe \u00f6, ue \u00fc, AE \u00c4, OE \u00d6, UE \u00dc, sz \u00df
zoneEditMessages = {
    rvZoneEditorDialogTitle : '\u00c4nderung der Zonenh\u00f6hen',
    rvZoneEditorAltitudeSwapped : "Die minimale H\u00f6he ist kleiner als die maximale H\u00f6he. Die Werte wurden vertauscht.",
};


L.drawLocal = {
    draw : {
        toolbar : {
            actions : {
                title : 'Zeichnen abbrechen',
                text : 'Abbrechen'
            },
            undo : {
                title : 'Letzten gezeichneten Punkt l\u00f6schen',
                text : 'Letzten Punkt l\u00f6schen'
            },
            buttons : {
                polyline : 'Zeichne eine Polylinie',
                polygon : 'Zeichne ein Polygon',
                rectangle : 'Zeichne ein Rechteck',
                circle : 'Zeichne einen Kreis',
                marker : 'Zeichne eine Markierung'
            }
        },
        handlers : {
            circle : {
                tooltip : {
                    start : 'Klicken und ziehen um einen Kreis zu zeichnen.'
                }
            },
            marker : {
                tooltip : {
                    start : 'Karte anklicken um die Markierung zu platzieren.'
                }
            },
            polygon : {
                tooltip : {
                    start : 'Anklicken um das Zeichen einer Figur zu beginnen.',
                    cont : 'Anklicken um das Zeichen einer Figur fortzusetzen.',
                    end : 'Ersten Punkt anklicken um die Figur abzuschlie\u00dfen.'
                }
            },
            polyline : {
                error : '<strong>Fehler:</strong> Figurkanten d\u00fcrfen sich nicht kreuzen!',
                tooltip : {
                    start : 'Anklicken um das Zeichen einer Linie zu beginnen.',
                    cont : 'Anklicken um das Zeichen einer Linie fortzusetzen.',
                    end : 'Letzen Punkt anklicken um die Linie abzuschlie\u00dfen.'
                }
            },
            rectangle : {
                tooltip : {
                    start : 'Klicken und ziehen um einen Rechteck zu zeichnen.'
                }
            },
            simpleshape : {
                tooltip : {
                    end : 'Maustaste loslassen um das Zeichnen abzuschlie\u00dfen.'
                }
            }
        }
    },
    edit : {
        toolbar : {
            actions : {
                save : {
                    title : '\u00c4nderungen speichern.',
                    text : 'Speichern'
                },
                cancel : {
                    title : 'Editieren abbrechen und alle \u00c4nderungen verwerfen.',
                    text : 'Abbrechen'
                }
            },
            buttons : {
                edit : 'Ebenen \u00e4ndern.',
                editDisabled : 'Keine Ebenen zu editieren.',
                remove : 'Ebenen l\u00f6schen.',
                removeDisabled : 'Keine Ebenen zu l\u00f6schen.'
            }
        },
        handlers : {
            edit : {
                tooltip : {
                    text : 'Punkte oder Markierung ziehen um die Form zu editieren.',
                    subtext : 'Abbrechen klicken um die \u00c4nderungen zu verwerfen.'
                }
            },
            remove : {
                tooltip : {
                    text : 'Form zum L\u00f6schen anklicken'
                }
            }
        }
    }
};

