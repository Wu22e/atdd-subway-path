package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addNewSection(Section section) {
        this.sections.add(section);
    }

    public void updateUpStationBetweenSection(Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(section -> section.equalUpStation(upStation))
                .findFirst()
                .ifPresent(section -> section.updateUpStation(downStation, distance));
    }

    public void updateDownStationBetweenSection(Station upStation, Station downStation, int distance) {
        this.sections.stream()
                .filter(section -> section.equalDownStation(downStation))
                .findFirst()
                .ifPresent(section -> section.updateDownStation(upStation, distance));
    }

    public Section findNextSection(Station station) {
        return this.sections.stream()
                .filter(section -> section.equalUpStation(station))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public Station findFirstUpStation() {
        Station upStation = this.sections.get(0).getUpStation();

        while (isPresentPrevSection(upStation)) {
            Section prevSection = findPrevSection(upStation);
            upStation = prevSection.getUpStation();
        }

        return upStation;
    }

    public boolean isPresentNextSection(Station station) {
        return this.sections.stream()
                .anyMatch(section -> section.equalUpStation(station));
    }

    public boolean isPresentPrevSection(Station upStation) {
        return this.sections.stream()
                .anyMatch(section -> section.equalDownStation(upStation));
    }

    public Section findPrevSection(Station upStation) {
        return this.sections.stream()
                .filter(section -> section.equalDownStation(upStation))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}