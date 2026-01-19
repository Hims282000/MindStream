package com.example.mindStreamApplication.Domain;


import jakarta.persistence.*;

@Entity
@Table(name = "tv_shows")
public class TvShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "album", nullable = false)
    private String album;

    @Column(name = "year")
    private Integer year;

    @Column(name = "chart_position")
    private String chartPosition;


    public TvShow() {
    }

    public TvShow(String album, Integer year, String chartPosition) {
        this.album = album;
        this.year = year;
        this.chartPosition = chartPosition;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getChartPosition() {
        return chartPosition;
    }

    public void setChartPosition(String chartPosition) {
        this.chartPosition = chartPosition;
    }

    @Override
    public String toString() {
        return "TvShow{" +
                "album='" + album + '\'' +
                ", id=" + id +
                ", year=" + year +
                ", chartPosition='" + chartPosition + '\'' +
                '}';
    }
}