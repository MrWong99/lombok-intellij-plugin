//skip compare content
@lombok.Builder
class BuilderValidConstructor {
  private int id;
  private String name;
}

@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
class BuilderValidConstructor2 {
  private int id;
  private String name;
}

@lombok.Builder
@lombok.AllArgsConstructor
@lombok.RequiredArgsConstructor
@lombok.NoArgsConstructor
class BuilderValidConstructor3 {
  private int id;
  private String name;
}

@lombok.Builder
@lombok.RequiredArgsConstructor
@lombok.NoArgsConstructor
class BuilderValidConstructor4 {
  private int id;
  private String name;

  BuilderValidConstructor4(int id, String name) {
    this.id = id;
    this.name = name;
  }
}

@lombok.Builder
@lombok.NoArgsConstructor
class BuilderValidConstructor5 {
  private int id;
  private String name;

  BuilderValidConstructor5(int id, String name) {
    this.id = id;
    this.name = name;
  }
}

<error descr="Lombok @Builder needs proper constructor for this class">@lombok.Builder</error>
@lombok.NoArgsConstructor
class BuilderInvalidConstructor {
  private int id;
  private String name;
}

<error descr="Lombok @Builder needs proper constructor for this class">@lombok.Builder</error>
@lombok.RequiredArgsConstructor
@lombok.NoArgsConstructor
class BuilderInvalidConstructor2 {
  private int id;
  private String name;
}

<error descr="Lombok @Builder needs proper constructor for this class">@lombok.Builder</error>
@lombok.RequiredArgsConstructor
@lombok.NoArgsConstructor
class BuilderInvalidConstructor3 {
  private int id;
  private String name;

  BuilderInvalidConstructor3(int id) {
    this.id = id;
  }
}

<error descr="Lombok @Builder needs proper constructor for this class">@lombok.Builder</error>
@lombok.NoArgsConstructor
class BuilderInvalidConstructor4 {
  private int id;
  private String name;

  BuilderInvalidConstructor4(int id) {
    this.id = id;
  }
}