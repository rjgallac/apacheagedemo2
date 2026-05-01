export default function TeamForm() {
    return (
        <div>
            <h4>Add Team</h4>

            <form>
                <label>Team Name:
                    <input type="text" name="teamName" />
                </label>
                <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Add Team</button>
            </form>
            <h4>link team to dept</h4>
            <h4>link team to person</h4>
        </div>
    );
}